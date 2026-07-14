package com.example.ticklev2.domain.reservation.service;

import com.example.ticklev2.domain.member.entity.Member;
import com.example.ticklev2.domain.member.repository.MemberRepository;
import com.example.ticklev2.domain.reservation.dto.response.ReservationConfirmResponseDto;
import com.example.ticklev2.domain.reservation.entity.PerformanceSeat;
import com.example.ticklev2.domain.reservation.entity.Reservation;
import com.example.ticklev2.domain.reservation.entity.ReservationSeat;
import com.example.ticklev2.domain.reservation.entity.SeatHold;
import com.example.ticklev2.domain.reservation.repository.ReservationRepository;
import com.example.ticklev2.domain.reservation.repository.ReservationSeatRepository;
import com.example.ticklev2.domain.venue.repository.SeatHoldRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final SeatHoldRepository seatHoldRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReservationConfirmResponseDto confirm(List<String> holdTokens, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        // 1. holdToken으로 SeatHold 조회 및 유효성 검증
        List<SeatHold> seatHolds = holdTokens.stream()
                .map(token -> seatHoldRepository.findByHoldToken(token)
                        .orElseThrow(() -> new IllegalStateException("유효하지 않은 선점 토큰입니다.")))
                .toList();

        // 2. 만료 여부 확인
        seatHolds.forEach(hold -> {
            if (hold.isExpired()) {
                throw new IllegalStateException("선점이 만료되었습니다.");
            }
        });

        // 3. 총 금액 계산
        int totalPrice = seatHolds.stream()
                .mapToInt(hold -> hold.getPerformanceSeat().getSeatPrice())
                .sum();

        // 4. Reservation 생성
        String reservationCode = "RSV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Reservation reservation = Reservation.create(member, totalPrice, reservationCode);

        reservationRepository.save(reservation);

        // 5. ReservationSeat 생성 (스냅샷), PerformanceSeat 상태 변경
        seatHolds.forEach(hold -> {
            PerformanceSeat ps = hold.getPerformanceSeat();
            ps.reserve(); // HELD → RESERVED

            ReservationSeat reservationSeat = ReservationSeat.create(
                    reservation,
                    ps,
                    ps.getSeatGrade(),
                    ps.getSeatRow(),
                    ps.getSeatNumber(),
                    ps.getSeatPrice()
            );
            reservationSeatRepository.save(reservationSeat);
        });

        // 6. SeatHold 삭제
        seatHoldRepository.deleteAll(seatHolds);

        // 7. Reservation 상태 확정
        reservation.confirm(); // PENDING → CONFIRMED

        return ReservationConfirmResponseDto.builder()
                .reservationId(reservation.getId())
                .reservationCode(reservationCode)
                .totalPrice(reservation.getTotalPrice())
                .build();
    }

    @Transactional
    public void cancel(Long reservationId, Long memberId) {

        // 1. Reservation 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 예매입니다."));

        // 2. 본인 예매인지 확인
        if (!reservation.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("본인의 예매만 취소할 수 있습니다.");
        }

        // 3. 예매 취소
        reservation.cancel(); // CONFIRMED → CANCELLED

        // 4. 좌석 상태 복구
        List<ReservationSeat> reservationSeats =
                reservationSeatRepository.findAllByReservationId(reservationId);

        reservationSeats.forEach(rs ->
                rs.getPerformanceSeat().release() // RESERVED → AVAILABLE
        );
    }
}
