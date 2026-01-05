package com.chil.ticketingservice.domain.search.dto.request;

import jakarta.validation.constraints.Pattern;

public record SearchCreateRequest(
        @Pattern(
                regexp = "^[가-힣a-zA-Z0-9 ]*$",
                message = "검색어는 한글, 영어, 숫자, 공백만 입력할 수 있습니다."
        )
        String searchData
) {
}
