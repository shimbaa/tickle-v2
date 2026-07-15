package com.example.ticklev2.domain.reservation.entity;

import com.example.ticklev2.domain.performance.entity.Performance;
import com.example.ticklev2.domain.venue.entity.Seat;
import com.example.ticklev2.global.exception.BusinessException;
import com.example.ticklev2.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "performance_seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "seat_grade", nullable = false, length = 10)
    private String seatGrade;

    @Column(name = "seat_row", nullable = false, length = 5)
    private String seatRow;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "seat_price", nullable = false)
    private Integer seatPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status", nullable = false, length = 10)
    private SeatStatus seatStatus;

    private PerformanceSeat(Performance performance, Seat seat, String seatGrade,
            String seatRow, String seatNumber, Integer seatPrice) {
        this.performance = performance;
        this.seat = seat;
        this.seatGrade = seatGrade;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.seatPrice = seatPrice;
        this.seatStatus = SeatStatus.AVAILABLE;
    }

    public static PerformanceSeat create(Performance performance, Seat seat, String seatGrade,
            String seatRow, String seatNumber, Integer seatPrice) {
        return new PerformanceSeat(performance, seat, seatGrade, seatRow, seatNumber, seatPrice);
    }

    public void hold() {
        if (this.seatStatus != SeatStatus.AVAILABLE) {
            throw new BusinessException(ErrorCode.SEAT_NOT_AVAILABLE);
        }
        this.seatStatus = SeatStatus.HELD;
    }

    public void release() {
        this.seatStatus = SeatStatus.AVAILABLE;
    }

    public void reserve() {
        if (this.seatStatus != SeatStatus.HELD) {
            throw new BusinessException(ErrorCode.SEAT_NOT_HELD);
        }
        this.seatStatus = SeatStatus.RESERVED;
    }
}
