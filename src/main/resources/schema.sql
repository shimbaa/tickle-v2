-- ==========================================
-- 1. 공연장 테이블 (마스터)
-- ==========================================
CREATE TABLE hall (
                      hall_id BIGSERIAL PRIMARY KEY,
                      hall_name VARCHAR(50) NOT NULL,
                      hall_address VARCHAR(100) NOT NULL,
                      hall_created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ==========================================
-- 2. 기본 좌석 테이블 (마스터)
-- ==========================================
CREATE TABLE seat (
                      seat_id BIGSERIAL PRIMARY KEY,
                      hall_id INTEGER NOT NULL REFERENCES hall(hall_id),
                      seat_grade VARCHAR(10) NOT NULL,
                      seat_row VARCHAR(5) NOT NULL,
                      seat_number VARCHAR(10) NOT NULL,
                      price INTEGER NOT NULL
);

-- ==========================================
-- 3. 회원 테이블
-- ==========================================
CREATE TABLE member (
                        member_id BIGSERIAL PRIMARY KEY,
                        member_email VARCHAR(50) NOT NULL UNIQUE, -- 로그인 ID 중복 방지 (무결성)
                        member_password VARCHAR(255) NOT NULL,
                        member_nickname VARCHAR(20) NOT NULL,
                        member_role VARCHAR(10) NOT NULL DEFAULT 'MEMBER',
                        member_created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        member_deleted_at TIMESTAMPTZ,
                        CONSTRAINT chk_member_role CHECK (member_role IN ('MEMBER','ADMIN')) -- 잘못된 권한 인입 방지 (무결성)
);

-- ==========================================
-- 4. 공연 테이블
-- ==========================================
CREATE TABLE performance (
                             performance_id BIGSERIAL PRIMARY KEY,
                             hall_id INTEGER NOT NULL REFERENCES hall(hall_id),
                             performance_title VARCHAR(100) NOT NULL,
                             performance_start_date TIMESTAMPTZ NOT NULL,
                             performance_end_date TIMESTAMPTZ NOT NULL,
                             performance_status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
                             performance_created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                             CONSTRAINT chk_performance_status
                                 CHECK (performance_status IN ('SCHEDULED','ON_SALE','SOLD_OUT','CANCELLED','COMPLETED'))
);

-- ==========================================
-- 5. 공연별 좌석 현황 테이블 (핵심 비즈니스 테이블)
-- ==========================================
CREATE TABLE performance_seat (
                                  performance_seat_id BIGSERIAL PRIMARY KEY,
                                  performance_id BIGINT NOT NULL REFERENCES performance(performance_id),
                                  seat_id INTEGER NOT NULL REFERENCES seat(seat_id),
                                  seat_grade VARCHAR(10) NOT NULL,
                                  seat_row VARCHAR(5) NOT NULL,
                                  seat_number VARCHAR(10) NOT NULL,
                                  seat_price INTEGER NOT NULL,
                                  seat_status VARCHAR(10) NOT NULL DEFAULT 'AVAILABLE',
                                  CONSTRAINT chk_seat_status CHECK (seat_status IN ('AVAILABLE','RESERVED')),

    -- [무결성 필수] 동일 공연에 동일 물리 좌석이 중복 등록되는 치명적인 데이터 오류를 물리적으로 차단
                                  CONSTRAINT uq_performance_seat UNIQUE (performance_id, seat_id)
);

-- ==========================================
-- 6. 좌석 임시 선점 테이블 (동시성 제어)
-- ==========================================
CREATE TABLE seat_hold (
                           seat_hold_id BIGSERIAL PRIMARY KEY,
    -- [무결성 필수] 한 좌석을 동시 유저가 선점하는 것을 DB 레벨에서 2중으로 원천 차단
                           performance_seat_id BIGINT NOT NULL UNIQUE REFERENCES performance_seat(performance_seat_id),
                           member_id BIGINT NOT NULL REFERENCES member(member_id),
                           hold_token VARCHAR(255) NOT NULL UNIQUE,
                           expires_at TIMESTAMPTZ NOT NULL,
                           created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ==========================================
-- 7. 예매 마스터 테이블
-- ==========================================
CREATE TABLE reservation (
                             reservation_id BIGSERIAL PRIMARY KEY,
                             member_id BIGINT NOT NULL REFERENCES member(member_id),
                             reservation_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                             total_price INTEGER NOT NULL,
                             reservation_code VARCHAR(30) NOT NULL UNIQUE, -- 예약 번호 중복 방지 (무결성)
                             reservation_created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                             reservation_updated_at TIMESTAMPTZ,
                             CONSTRAINT chk_reservation_status
                                 CHECK (reservation_status IN ('PENDING','CONFIRMED','CANCELLED'))
);

-- ==========================================
-- 8. 예매 상세 좌석 정보 테이블
-- ==========================================
CREATE TABLE reservation_seat (
                                  reservation_seat_id BIGSERIAL PRIMARY KEY,
                                  reservation_id BIGINT NOT NULL REFERENCES reservation(reservation_id),
                                  performance_seat_id BIGINT NOT NULL REFERENCES performance_seat(performance_seat_id),
                                  seat_grade VARCHAR(10) NOT NULL,
                                  seat_row VARCHAR(5) NOT NULL,
                                  seat_number VARCHAR(10) NOT NULL,
                                  seat_price INTEGER NOT NULL,

    -- [무결성 필수] 하나의 주문(장바구니) 내에 같은 좌석이 중복으로 담기는 현상 방지
                                  CONSTRAINT uq_reservation_seat UNIQUE (reservation_id, performance_seat_id)
);


-- ==========================================
-- 🚀 추후 단계적 성능 향상을 위한 인덱스 정의 (현재 주석 처리)
-- ==========================================

-- [기본 상태 검증 포인트]
-- 아래 인덱스들이 없는 상태에서 대량의 데이터를 넣고 쿼리를 날리면 'Seq Scan(전체 테이블 스캔)'이 발생합니다.
-- 추후 부하 테스트 시 인덱스를 하나씩 해제(Uncomment)하며 성능 변곡점을 모니터링하기 위한 용도입니다.

-- 외래키(FK) 연관 관계 조회 최적화용 단일 인덱스들
-- CREATE INDEX idx_seat_hall_id ON seat(hall_id);
-- CREATE INDEX idx_performance_hall_id ON performance(hall_id);
-- CREATE INDEX idx_reservation_member_id ON reservation(member_id);
-- CREATE INDEX idx_reservation_seat_res_id ON reservation_seat(reservation_id);

-- [핵심 최적화 대상 1] 예매창 진입 시 "특정 공연의 남은 좌석 목록"을 초당 수만 명의 유저가 새로고침할 때
-- 복합 인덱스(performance_id, seat_status) 유무에 따른 조회 속도(Index Scan) 차이를 극적으로 비교할 수 있습니다.
-- CREATE INDEX idx_performance_seat_perf_status ON performance_seat(performance_id, seat_status);

-- [핵심 최적화 대상 2] 5분간 결제되지 않은 선점 좌석을 1초~10초 주기 스케줄러가 대량으로 조회하여 삭제할 때
-- 시계열 데이터인 만료 시간 정렬 및 조건 검색 성능을 비교할 수 있습니다.
-- CREATE INDEX idx_seat_hold_expires_at ON seat_hold(expires_at);