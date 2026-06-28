package com.example.ticklev2.domain.venue.repository;

import com.example.ticklev2.domain.venue.entity.Seat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByHallId(Long hallId);
}
