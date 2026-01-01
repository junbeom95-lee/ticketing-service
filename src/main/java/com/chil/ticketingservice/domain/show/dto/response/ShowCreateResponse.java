package com.chil.ticketingservice.domain.show.dto.response;

import com.chil.ticketingservice.domain.show.entity.Show;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ShowCreateResponse {

    private final Long id;
    private final String title;
    private final String location;
    private final LocalDateTime showDate;
    private final Long ageRating;
    private final String description;
    private final String imageUrl;
    private final LocalDateTime createdAt;

    public static ShowCreateResponse from(Show show) {
        return new ShowCreateResponse(
                show.getId(),
                show.getTitle(),
                show.getLocation(),
                show.getShowDate(),
                show.getAgeRating(),
                show.getDescription(),
                show.getImageUrl(),
                show.getCreatedAt()
        );
    }
}
