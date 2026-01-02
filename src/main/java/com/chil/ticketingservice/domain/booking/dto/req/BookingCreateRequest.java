package com.chil.ticketingservice.domain.booking.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookingCreateRequest(
        @NotNull(message = "공연 ID는 필수입니다")
        Long showId,

        @NotBlank(message = "좌석 정보는 필수입니다")
        String seat,

        @NotNull(message = "가격 정보는 필수입니다")
        Integer price
) {
}
