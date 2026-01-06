package com.chil.ticketingservice.domain.show.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "searchlogs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long id;

    private String showTitle;

    private Long userId;

    private LocalDateTime searchTime;

    public SearchLog(String showTitle, Long userId, LocalDateTime searchTime) {
        this.showTitle = showTitle;
        this.userId = userId;
        this.searchTime = searchTime;
    }
}
