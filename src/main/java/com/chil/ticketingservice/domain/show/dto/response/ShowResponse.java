package com.chil.ticketingservice.domain.show.dto.response;

import com.chil.ticketingservice.domain.show.entity.Show;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ShowResponse {

    private final Long showId;
    private final String title;
    private final String location;
    private final LocalDateTime showDate;
    private final String imageUrl;
    private final Long likes;

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
