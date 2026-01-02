package com.chil.ticketingservice.domain.price.dto.response;

import com.chil.ticketingservice.domain.price.entity.Price;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;

public record ShowSeatPriceRegResponse(
    Long showId,
    SeatTypeEnum seatType,
    int price
) {

    public static ShowSeatPriceRegResponse from(Price seatPrice) {
        return new ShowSeatPriceRegResponse(
                seatPrice.getShow().getId(),
                seatPrice.getSeatType(),
                seatPrice.getPrice()
        );
    }
}
