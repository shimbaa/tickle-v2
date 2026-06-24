package com.example.ticklev2.domain.reservation.entity;

import com.example.ticklev2.domain.member.entity.Member;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false, length = 20)
    private ReservationStatus status;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "reservation_code", nullable = false, unique = true, length = 30)
    private String reservationCode;

    @Column(name = "reservation_created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "reservation_updated_at")
    private OffsetDateTime updatedAt;

    private Reservation(Member member, Integer totalPrice, String reservationCode) {
        this.member = member;
        this.totalPrice = totalPrice;
        this.reservationCode = reservationCode;
        this.status = ReservationStatus.PENDING;
        this.createdAt = OffsetDateTime.now();
    }

    public static Reservation create(Member member, Integer totalPrice, String reservationCode) {
        return new Reservation(member, totalPrice, reservationCode);
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
        this.updatedAt = OffsetDateTime.now();
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
        this.updatedAt = OffsetDateTime.now();
    }
}
