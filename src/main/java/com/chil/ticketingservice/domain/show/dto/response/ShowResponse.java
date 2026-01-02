package com.chil.ticketingservice.domain.show.dto.response;

import com.chil.ticketingservice.domain.show.entity.Show;

import java.time.LocalDateTime;

public record ShowResponse(
    Long showId,
    String title,
    String location,
    LocalDateTime showDate,
    String imageUrl,
    Long likes
) {

    public static ShowResponse from(Show show) {
        return new ShowResponse(
                show.getId(),
                show.getTitle(),
                show.getLocation(),
                show.getShowDate(),
                show.getImageUrl(),
                0L
        );
    }
}
