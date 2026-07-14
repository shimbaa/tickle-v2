package com.example.ticklev2.domain.reservation.repository;

import com.example.ticklev2.domain.reservation.entity.PerformanceSeat;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceSeatRepository extends JpaRepository<PerformanceSeat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ps FROM PerformanceSeat ps WHERE ps.id = :performanceSeatId AND ps.seatStatus = 'AVAILABLE'")
    Optional<PerformanceSeat> findAvailableSeatWithLock(@Param("performanceSeatId") Long performanceSeatId);

}
