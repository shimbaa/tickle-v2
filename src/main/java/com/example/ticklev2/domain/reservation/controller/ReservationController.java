package com.example.ticklev2.domain.reservation.controller;

import com.example.ticklev2.domain.reservation.dto.request.ReservationCancelRequestDto;
import com.example.ticklev2.domain.reservation.dto.request.ReservationConfirmRequestDto;
import com.example.ticklev2.domain.reservation.dto.response.ReservationConfirmResponseDto;
import com.example.ticklev2.domain.reservation.service.ReservationService;
import com.example.ticklev2.global.response.ResultCode;
import com.example.ticklev2.global.response.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/confirm")
    public ResponseEntity<ResultResponse<ReservationConfirmResponseDto>> confirm(
            @RequestBody @Valid ReservationConfirmRequestDto request) {

        ReservationConfirmResponseDto response = reservationService.confirm(
                request.getHoldTokens(),
                request.getMemberId()
        );

        return ResponseEntity
                .status(ResultCode.RESERVATION_CONFIRM_SUCCESS.getStatus())
                .body(ResultResponse.of(ResultCode.RESERVATION_CONFIRM_SUCCESS, response));
    }

    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<ResultResponse<Void>> cancel(
            @PathVariable Long reservationId,
            @RequestBody @Valid ReservationCancelRequestDto request) {

        reservationService.cancel(reservationId, request.getMemberId());

        return ResponseEntity
                .status(ResultCode.RESERVATION_CANCEL_SUCCESS.getStatus())
                .body(ResultResponse.of(ResultCode.RESERVATION_CANCEL_SUCCESS));
    }
}
