package com.example.ticklev2.domain.venue.repository;

import com.example.ticklev2.domain.reservation.entity.SeatHold;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatHoldRepository extends JpaRepository<SeatHold, Long> {

    List<SeatHold> findAllByExpiresAtBefore(OffsetDateTime now);

    Optional<SeatHold> findByPerformanceSeatId(Long performanceSeatId);
}
