package com.seongho.couponrush.entity;

import com.seongho.couponrush.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private LocalDate birthDate;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Member(
            String loginId,
            String password,
            MemberRole memberRole,
            String name,
            String email,
            LocalDate birthDate
    ){
        this.loginId = loginId;
        this.password = password;
        this.memberRole = memberRole;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.createdAt = LocalDateTime.now();
    }
    public static Member createUser(
            String loginId,
            String password,
            String name,
            String email,
            LocalDate birthDate
    ){
        return new Member(
                loginId,
                password,
                MemberRole.USER,
                name,
                email,
                birthDate
        );
    }
}
