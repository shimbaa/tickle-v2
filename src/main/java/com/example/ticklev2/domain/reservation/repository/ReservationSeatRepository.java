package com.example.ticklev2.domain.reservation.repository;

import com.example.ticklev2.domain.reservation.entity.ReservationSeat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {

    List<ReservationSeat> findAllByReservationId(Long reservationId);
}
