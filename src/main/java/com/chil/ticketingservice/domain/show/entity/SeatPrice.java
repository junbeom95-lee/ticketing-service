package com.chil.ticketingservice.domain.show.entity;

import com.chil.ticketingservice.domain.show.enums.SeatTypeEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SeatTypeEnum seatType;

    @Column(nullable = false)
    private Integer price;

    public SeatPrice(Show show, SeatTypeEnum seatType, Integer price) {
        this.show = show;
        this.seatType = seatType;
        this.price = price;
    }
}
