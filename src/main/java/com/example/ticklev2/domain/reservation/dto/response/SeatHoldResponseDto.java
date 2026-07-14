package com.example.ticklev2.domain.reservation.dto.response;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@NotNull
public class SeatHoldResponseDto {

    private List<String> holdTokens;
}