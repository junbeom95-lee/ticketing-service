package com.chil.ticketingservice.domain.price.entity;

import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "prices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {

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

    public Price(Show show, SeatTypeEnum seatType, Integer price) {
        this.show = show;
        this.seatType = seatType;
        this.price = price;
    }
}
