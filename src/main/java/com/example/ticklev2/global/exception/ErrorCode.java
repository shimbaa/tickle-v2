package com.example.ticklev2.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "잘못된 타입 값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    DATA_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 처리 중 오류가 발생했습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),

    INVALID_HOLD_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰 값입니다."),
    SEAT_ALREADY_HELD(HttpStatus.CONFLICT, "이미 선점되었거나 존재하지 않는 좌석입니다."),
    SEAT_HOLD_EXPIRED(HttpStatus.CONFLICT, "선점이 만료되었습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예매 정보를 찾을 수 없습니다."),
    RESERVATION_CANCEL_FORBIDDEN(HttpStatus.FORBIDDEN, "본인의 예매만 취소할 수 있습니다.");

    private final HttpStatus status;
    private final String message;
}
