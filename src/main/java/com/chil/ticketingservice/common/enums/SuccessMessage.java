package com.chil.ticketingservice.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    AUTH_LOGIN_SUCCESS("로그인 성공"),

    LIKE_CREATE_SUCCESS("좋아요 생성 성공"),
    LIKE_DELETE_SUCCESS("좋아요 삭제 성공"),
    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
