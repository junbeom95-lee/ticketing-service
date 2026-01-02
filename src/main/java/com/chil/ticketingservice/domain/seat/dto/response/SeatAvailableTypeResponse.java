package com.chil.ticketingservice.domain.seat.dto.response;

import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;

public record SeatAvailableTypeResponse(SeatTypeEnum seatType, Long seats) {
}
