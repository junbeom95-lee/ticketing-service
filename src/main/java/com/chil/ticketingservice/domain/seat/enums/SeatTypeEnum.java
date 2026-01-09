package com.chil.ticketingservice.domain.seat.enums;

import lombok.Getter;

@Getter
public enum SeatTypeEnum {

    VIP(100),
    S(500),
    A(1000),
    B(1000),

    ;

    SeatTypeEnum(Integer size) {
        this.size = size;
    }

    private final Integer size;
}
