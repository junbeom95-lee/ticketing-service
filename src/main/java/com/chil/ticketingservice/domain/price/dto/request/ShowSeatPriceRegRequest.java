package com.chil.ticketingservice.domain.price.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShowSeatPriceRegRequest(
    @NotBlank(message = "좌석 타입이 입력 되지 않았습니다.")
    String seatType,

    @NotNull(message = "가격이 입력 되지 않았습니다.")
    int price
) {

}
