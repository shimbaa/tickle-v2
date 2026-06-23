package com.example.ticklev2.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_seat_id", nullable = false)
    private PerformanceSeat performanceSeat;

    @Column(name = "seat_grade", nullable = false, length = 10)
    private String seatGrade;

    @Column(name = "seat_row", nullable = false, length = 5)
    private String seatRow;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "seat_price", nullable = false)
    private Integer seatPrice;

    private ReservationSeat(Reservation reservation, PerformanceSeat performanceSeat,
            String seatGrade, String seatRow, String seatNumber, Integer seatPrice) {
        this.reservation = reservation;
        this.performanceSeat = performanceSeat;
        this.seatGrade = seatGrade;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.seatPrice = seatPrice;
    }

    public static ReservationSeat create(Reservation reservation, PerformanceSeat performanceSeat,
            String seatGrade, String seatRow, String seatNumber, Integer seatPrice) {
        return new ReservationSeat(reservation, performanceSeat, seatGrade, seatRow, seatNumber, seatPrice);
    }
}
