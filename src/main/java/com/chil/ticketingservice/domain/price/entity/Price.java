package com.chil.ticketingservice.domain.price.entity;

import com.chil.ticketingservice.domain.show.entity.Show;
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

    @ManyToOne
    @JoinColumn(name="show_id", nullable = false)
    private Show show;

    @Column(nullable = false)
    private String seatType;

    @Column(nullable = false)
    private int seatPrice;
}
