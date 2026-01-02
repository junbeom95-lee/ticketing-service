package com.chil.ticketingservice.domain.price.dto.response;

import com.chil.ticketingservice.domain.price.entity.Price;
import com.chil.ticketingservice.domain.price.enums.SeatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShowSeatPriceRegResponse {

    private final Long showId;
    private final SeatTypeEnum seatType;
    private final Integer price;

    public static ShowSeatPriceRegResponse from(Price seatPrice) {
        return new ShowSeatPriceRegResponse(
                seatPrice.getShow().getId(),
                seatPrice.getSeatType(),
                seatPrice.getPrice()
        );
    }
}
