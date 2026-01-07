package com.chil.ticketingservice.domain.show.dto.request;

import java.time.LocalDateTime;

public record ShowSearchLogRequest(
        Long userId,
        String title,
        LocalDateTime searchedAt
) {
}
