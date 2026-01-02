package com.chil.ticketingservice.domain.price.dto.response;

import com.chil.ticketingservice.domain.price.entity.Price;
import com.chil.ticketingservice.domain.price.enums.SeatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
