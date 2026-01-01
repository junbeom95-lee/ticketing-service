package com.chil.ticketingservice.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    AUTH_LOGIN_SUCCESS("로그인 성공"),
    USER_CREATE_SUCCESS("회원가입 성공"),

    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
