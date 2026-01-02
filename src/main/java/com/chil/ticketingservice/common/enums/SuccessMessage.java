package com.chil.ticketingservice.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    AUTH_LOGIN_SUCCESS("로그인 성공"),
    USER_CREATE_SUCCESS("회원가입 성공"),
    USER_GET_SUCCESS("회원 조회 성공"),

    // 공연
    SHOW_CREATE_SUCCESS("공연이 생성되었습니다"),
    SHOW_RESPONSE_SUCCESS("공연 검색 성공"),
    SHOW_CREATE_SEAT_SUCCESS("공연 좌석 가격이 생성 되었습니다.")

    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
