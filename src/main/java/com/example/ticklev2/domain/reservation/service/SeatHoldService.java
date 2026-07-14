package com.example.ticklev2.domain.reservation.service;


import com.example.ticklev2.domain.member.entity.Member;
import com.example.ticklev2.domain.member.repository.MemberRepository;
import com.example.ticklev2.domain.reservation.dto.response.SeatHoldResponseDto;
import com.example.ticklev2.domain.reservation.entity.PerformanceSeat;
import com.example.ticklev2.domain.reservation.entity.SeatHold;
import com.example.ticklev2.domain.reservation.repository.PerformanceSeatRepository;
import com.example.ticklev2.domain.venue.repository.SeatHoldRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatHoldService {

    private final PerformanceSeatRepository performanceSeatRepository;
    private final SeatHoldRepository seatHoldRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public SeatHoldResponseDto hold(List<Long> performanceSeatIds, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        List<Long> sortedIds = performanceSeatIds.stream()
                .sorted()
                .toList();

        List<SeatHold> seatHolds = new ArrayList<>();

        for (Long seatId : sortedIds) {
            PerformanceSeat performanceSeat = performanceSeatRepository
                    .findAvailableSeatWithLock(seatId)
                    .orElseThrow(() -> new IllegalStateException("이미 선점되었거나 존재하지 않는 좌석입니다."));

            performanceSeat.hold();

            SeatHold seatHold = SeatHold.create(performanceSeat, member);
            seatHolds.add(seatHold);
        }

        seatHoldRepository.saveAll(seatHolds);

        return new SeatHoldResponseDto(
                seatHolds.stream()
                        .map(SeatHold::getHoldToken)
                        .toList()
        );
    }
}
