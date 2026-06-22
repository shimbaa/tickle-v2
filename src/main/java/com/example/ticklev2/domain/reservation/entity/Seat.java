package com.example.ticklev2.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @Column(name = "seat_grade", nullable = false, length = 10)
    private String seatGrade;

    @Column(name = "seat_row", nullable = false, length = 5)
    private String seatRow;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "price", nullable = false)
    private Integer price;

    private Seat(Hall hall, String seatGrade, String seatRow, String seatNumber, Integer price) {
        this.hall = hall;
        this.seatGrade = seatGrade;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public static Seat create(Hall hall, String seatGrade, String seatRow, String seatNumber, Integer price) {
        return new Seat(hall, seatGrade, seatRow, seatNumber, price);
    }
}
