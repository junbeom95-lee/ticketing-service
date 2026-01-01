package com.chil.ticketingservice.domain.booking.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookingCreateRequest {

    @NotNull(message = "공연 ID는 필수입니다")
    private Long showId;

    @NotBlank(message = "좌석 정보는 필수입니다")
    private String seat;

    @NotNull(message = "가격 정보는 필수입니다")
    private Integer price;
}
