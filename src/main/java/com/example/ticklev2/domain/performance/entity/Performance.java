package com.example.ticklev2.domain.performance.entity;

import com.example.ticklev2.domain.venue.entity.Hall;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "performance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @Column(name = "performance_title", nullable = false, length = 100)
    private String title;

    @Column(name = "performance_start_date", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "performance_end_date", nullable = false)
    private OffsetDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "performance_status", nullable = false, length = 20)
    private PerformanceStatus status;

    @Column(name = "performance_created_at", nullable = false)
    private OffsetDateTime createdAt;

    private Performance(Hall hall, String title, OffsetDateTime startDate, OffsetDateTime endDate) {
        this.hall = hall;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = PerformanceStatus.SCHEDULED;
        this.createdAt = OffsetDateTime.now();
    }

    public static Performance create(Hall hall, String title, OffsetDateTime startDate, OffsetDateTime endDate) {
        return new Performance(hall, title, startDate, endDate);
    }
}
