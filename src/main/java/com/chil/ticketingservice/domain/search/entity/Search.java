package com.chil.ticketingservice.domain.search.entity;

import com.chil.ticketingservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "searchs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long id;

    private String searchData;

    private Long userId;

    public Search(String searchData, Long userId) {
        this.searchData = searchData;
        this.userId = userId;
    }
}
