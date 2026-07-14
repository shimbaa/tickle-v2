package com.example.ticklev2.domain.reservation.controller;

import com.example.ticklev2.domain.reservation.dto.request.SeatHoldRequestDto;
import com.example.ticklev2.domain.reservation.dto.response.SeatHoldResponseDto;
import com.example.ticklev2.domain.reservation.entity.SeatHold;
import com.example.ticklev2.domain.reservation.service.SeatHoldService;
import com.example.ticklev2.global.response.ResultCode;
import com.example.ticklev2.global.response.ResultResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatHoldController {

    private final SeatHoldService seatHoldService;

    @PostMapping("/hold")
    public ResponseEntity<ResultResponse<SeatHoldResponseDto>> hold(
            @RequestBody @Valid SeatHoldRequestDto request) {

        SeatHoldResponseDto response = seatHoldService.hold(
                request.getPerformanceSeatIds(),
                request.getMemberId()
        );



        return ResponseEntity
                .status(ResultCode.SEAT_HOLD_SUCCESS.getStatus())
                .body(ResultResponse.of(ResultCode.SEAT_HOLD_SUCCESS,
                        response));
    }
}
