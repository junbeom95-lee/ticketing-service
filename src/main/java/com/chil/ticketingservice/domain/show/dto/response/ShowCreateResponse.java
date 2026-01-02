package com.chil.ticketingservice.domain.show.dto.response;

import com.chil.ticketingservice.domain.show.entity.Show;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public record ShowCreateResponse(
    Long id,
    String title,
    String location,
    LocalDateTime showDate,
    Long ageRating,
    String description,
    String imageUrl,
    LocalDateTime createdAt
) {

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
