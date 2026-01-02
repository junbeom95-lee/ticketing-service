package com.chil.ticketingservice.domain.seat.entity;

import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.show.entity.Show;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatTypeEnum seatType;

    @Column(nullable = false)
    private Integer seatNumber;

    @Column(nullable = false)
    private Boolean seatStatus;

    public Seat (Show show, SeatTypeEnum seatType, Integer seatNumber) {
        this.show = show;
        this.seatType = seatType;
        this.seatNumber = seatNumber;
        this.seatStatus = true;
    }
}
