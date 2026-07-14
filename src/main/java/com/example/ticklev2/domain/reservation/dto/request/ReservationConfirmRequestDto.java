package com.example.ticklev2.domain.reservation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationConfirmRequestDto {

    @NotEmpty
    private List<String> holdTokens;

    @NotNull
    private Long memberId;
}
