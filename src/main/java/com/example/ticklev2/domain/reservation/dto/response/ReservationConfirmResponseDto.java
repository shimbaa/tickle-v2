package com.example.ticklev2.domain.reservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReservationConfirmResponseDto {

    private Long reservationId;
    private String reservationCode;
    private Integer totalPrice;
}
