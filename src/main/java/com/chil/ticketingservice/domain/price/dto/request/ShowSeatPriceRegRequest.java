package com.chil.ticketingservice.domain.price.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeatPriceRegRequest {

    @NotBlank(message = "좌석 타입이 입력 되지 않았습니다.")
    /*@Pattern(
            regexp = "^(VIP|S|A|B)$",
            message = "좌석 타입은 VIP, S, A, B 중 하나여야 합니다."
    )*/
    private String seatType;

    @NotNull(message = "가격이 입력 되지 않았습니다.")
    private Integer price;
}
