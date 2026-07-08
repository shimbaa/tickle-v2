package com.example.ticklev2.domain.reservation.scheduler;

import com.example.ticklev2.domain.reservation.entity.SeatHold;
import com.example.ticklev2.domain.venue.repository.SeatHoldRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatHoldScheduler {

    private final SeatHoldRepository seatHoldRepository;

    @Scheduled(fixedDelay = 10000) // 10초마다 실행
    @Transactional
    public void releaseExpiredHolds() {
        List<SeatHold> expiredHolds = seatHoldRepository
                .findAllByExpiresAtBefore(OffsetDateTime.now());

        if (expiredHolds.isEmpty()) return;

        expiredHolds.forEach(hold -> {
            hold.getPerformanceSeat().release(); // HELD → AVAILABLE
            log.info("선점 만료 해제: performanceSeatId={}", hold.getPerformanceSeat().getId());
        });

        seatHoldRepository.deleteAll(expiredHolds);

        log.info("만료된 선점 {}건 해제 완료", expiredHolds.size());
    }

}
