package com.chil.ticketingservice.domain.price.dto.response;

import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;

public record PriceShowSeatResponse(SeatTypeEnum seatType, Integer price) {
}
