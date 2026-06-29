package com.example.ticklev2.domain.reservation.repository;

import com.example.ticklev2.domain.reservation.entity.PerformanceSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceSeatRepository extends JpaRepository<PerformanceSeat, Long> {
}
