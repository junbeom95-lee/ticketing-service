package com.chil.ticketingservice.domain.user.dto.response;

import com.chil.ticketingservice.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserGetResponse(
        Long id,
        String email,
        String username,
        LocalDate birth,
        LocalDateTime createdAt
) {
    public static UserGetResponse from(User user) {
        return new UserGetResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getBirth(),
                user.getCreatedAt()
        );
    }
}