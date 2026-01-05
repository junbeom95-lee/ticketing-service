package com.chil.ticketingservice.domain.show.entity;

import com.chil.ticketingservice.common.entity.BaseEntity;
import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "shows")
@NoArgsConstructor(access =AccessLevel.PROTECTED)
public class Show extends BaseEntity {

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

    @Column
    private String imageUrl;

    public Show(ShowCreateRequest request) {
        this.title = request.title();
        this.location = request.location();
        this.showDate = request.showDate();
        this.ageRating = request.ageRating();
        this.description = request.description();
    }
}
