package com.example.ticklev2.domain.venue.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "hall")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hall_id")
    private Long id;

    @Column(name = "hall_name", nullable = false, length = 50)
    private String name;


    @Column(name = "hall_address", nullable = false, length = 100)
    private String address;

    @Column(name = "hall_created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
