package com.chil.ticketingservice.domain.show.dto.response;

import com.chil.ticketingservice.domain.show.entity.SeatPrice;
import com.chil.ticketingservice.domain.show.enums.SeatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShowSeatPriceRegResponse {

    private final Long showId;
    private final SeatTypeEnum seatType;
    private final Integer price;

    public static ShowSeatPriceRegResponse from(SeatPrice seatPrice) {
        return new ShowSeatPriceRegResponse(
                seatPrice.getShow().getId(),
                seatPrice.getSeatType(),
                seatPrice.getPrice()
        );
    }
}
