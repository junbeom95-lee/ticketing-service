package com.chil.ticketingservice.domain.show.entity;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.entity.BaseEntity;
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

    @Column(nullable = false)
    private Long creator = 1L; // 관리자의 ROLE 을 가지고 있는 애들이 들어가야 하기 때문에 USER 병합 전 상수 처리

    @Column
    private String imageUrl;

    public Show(
            String title,
            String location,
            LocalDateTime showDate,
            Long ageRating,
            String description
    ) {
        this.title = title;
        this.location = location;
        this.showDate = showDate;
        this.ageRating = ageRating;
        this.description = description;
    }

    // 공연 삭제 메서드
    public void showDelete(Long id) {
        this.id = id;
    }
}
