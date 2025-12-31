package com.chil.ticketingservice.domain.show.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "shows")
@NoArgsConstructor(access =AccessLevel.PROTECTED)
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime showDate;

    @Column(nullable = false)
    private Long ageRating;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long creator;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime createAt;

    public Show(
            String title,
            String location,
            LocalDateTime showDate,
            Long ageRating,
            String description,
            Long creator
    ) {
        this.title = title;
        this.location = location;
        this.showDate = showDate;
        this.ageRating = ageRating;
        this.description = description;
        this.creator = creator;
    }

    // 공연 삭제
    public void showDelete(Long id) {
        this.id = id;
    }
}
