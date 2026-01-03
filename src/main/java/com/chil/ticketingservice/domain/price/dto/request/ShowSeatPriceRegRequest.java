package com.chil.ticketingservice.domain.price.dto.request;

import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import jakarta.validation.constraints.NotNull;

public record ShowSeatPriceRegRequest(
    @NotNull(message = "좌석 타입이 입력 되지 않았습니다.")
    SeatTypeEnum seatType,

    @NotNull(message = "가격이 입력 되지 않았습니다.")
    Integer price
) {

}
