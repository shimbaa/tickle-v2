package com.example.ticklev2.domain.reservation.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seat_hold")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_hold_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_seat_id", nullable = false, unique = true)
    private PerformanceSeat performanceSeat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "hold_token", nullable = false, unique = true, length = 255)
    private String holdToken;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    private static final int HOLD_DURATION_MINUTES = 5;

    private SeatHold(PerformanceSeat performanceSeat, Member member) {
        this.performanceSeat = performanceSeat;
        this.member = member;
        this.holdToken = UUID.randomUUID().toString();
        this.createdAt = OffsetDateTime.now();
        this.expiresAt = this.createdAt.plusMinutes(HOLD_DURATION_MINUTES);
    }

    public static SeatHold create(PerformanceSeat performanceSeat, Member member) {
        return new SeatHold(performanceSeat, member);
    }

    public boolean isExpired() {
        return OffsetDateTime.now().isAfter(expiresAt);
    }
}
