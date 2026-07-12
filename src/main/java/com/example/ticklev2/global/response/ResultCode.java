package com.example.ticklev2.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // 좌석 조회
    PERFORMANCE_SEATS_SUCCESS(HttpStatus.OK, "공연 좌석 조회 성공"),

    // 선점
    SEAT_HOLD_SUCCESS(HttpStatus.OK, "좌석 선점 성공"),
    SEAT_HOLD_FAILURE(HttpStatus.CONFLICT, "좌석 선점 실패"),

    // 예매
    RESERVATION_CONFIRM_SUCCESS(HttpStatus.CREATED, "예매 확정 성공"),
    RESERVATION_CANCEL_SUCCESS(HttpStatus.OK, "예매 취소 성공"),
    RESERVATION_LIST_SUCCESS(HttpStatus.OK, "예매 내역 조회 성공"),
    RESERVATION_DETAIL_SUCCESS(HttpStatus.OK, "예매 상세 조회 성공");

    private final HttpStatus status;
    private final String message;
}
