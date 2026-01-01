package com.chil.ticketingservice.domain.user.dto.response;

import com.chil.ticketingservice.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateResponse {

    // todo: Token 추가 예정

    private final Long id;

    public static UserCreateResponse from(User user) {
        return new UserCreateResponse(
                user.getId()
        );
    }
}
