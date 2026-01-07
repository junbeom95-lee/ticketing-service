package com.chil.ticketingservice.domain.log.entity;

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

    private Long userId;

    private String showTitle;

    private LocalDateTime searchTime;

    public SearchLog(Long userId, String showTitle, LocalDateTime searchTime) {
        this.userId = userId;
        this.showTitle = showTitle;
        this.searchTime = searchTime;
    }
}
