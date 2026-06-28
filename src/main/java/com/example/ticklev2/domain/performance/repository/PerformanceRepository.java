package com.example.ticklev2.domain.performance.repository;

import com.example.ticklev2.domain.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

}
