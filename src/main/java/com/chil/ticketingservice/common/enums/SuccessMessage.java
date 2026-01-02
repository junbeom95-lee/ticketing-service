package com.chil.ticketingservice.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    AUTH_LOGIN_SUCCESS("로그인 성공"),

    BOOKING_CREATE_SUCCESS("예매 성공"),
    BOOKING_CANCEL_SUCCESS("예매 취소 성공"),

    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
