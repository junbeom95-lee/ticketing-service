package com.chil.ticketingservice.domain.user.entity;

import com.chil.ticketingservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 50, nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String role = "USER";

    public User(String email, String username, LocalDate birth, String password) {
        this.email = email;
        this.username = username;
        this.birth = birth;
        this.password = password;
        this.role = role;
    }
}
