package com.example.ticklev2.domain.reservation.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "member_password", nullable = false, length = 255)
    private String password;

    @Column(name = "member_nickname", nullable = false, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false, length = 10)
    private MemberRole role;

    @Column(name = "member_created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "member_deleted_at")
    private OffsetDateTime deletedAt;

    private Member(String email, String password, String nickname, MemberRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.createdAt = OffsetDateTime.now();
    }

    public static Member create(String email, String password, String nickname, MemberRole role) {
        return new Member(email, password, nickname, role);
    }
}
