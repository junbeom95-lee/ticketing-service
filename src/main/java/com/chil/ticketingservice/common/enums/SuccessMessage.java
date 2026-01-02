package com.chil.ticketingservice.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    AUTH_LOGIN_SUCCESS("로그인 성공."),
    USER_CREATE_SUCCESS("회원가입 성공."),
    USER_GET_SUCCESS("회원 조회 성공."),

    // 공연
    SHOW_CREATE_SUCCESS("공연이 생성되었습니다."),
    SHOW_RESPONSE_SUCCESS("공연 검색 성공."),
    SHOW_CREATE_SEAT_SUCCESS("공연 좌석 가격이 생성 되었습니다."),

    // 예매
    BOOKING_CREATE_SUCCESS("예매 성공."),
    BOOKING_CANCEL_SUCCESS("예매 취소 성공."),
    BOOKING_GET_SUCCESS("예매 조회 성공"),

    LIKE_CREATE_SUCCESS("좋아요가 생성되었습니다."),
    LIKE_DELETE_SUCCESS("좋아요가 삭제되었습니다."),
    LIKE_COUNT_SUCCESS("좋아요 개수를 조회했습니다."),
    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
