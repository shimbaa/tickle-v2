package com.example.ticklev2.domain.reservation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatHoldRequestDto {

    @NotEmpty
    private List<Long> performanceSeatIds;

    @NotNull
    private Long memberId;
}
