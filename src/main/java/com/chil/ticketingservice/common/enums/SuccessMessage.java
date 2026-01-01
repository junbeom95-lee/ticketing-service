package com.chil.ticketingservice.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    AUTH_LOGIN_SUCCESS("로그인 성공"),

    // 공연
    SHOW_CREATE_SUCCESS("공연이 생성되었습니다"),
    SHOW_RESPONSE_SUCCESS("공연 검색 성공")

    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
