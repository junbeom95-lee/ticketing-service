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
    BOOKING_GET_SUCCESS("예매 조회 성공."),

    //좋아요
    LIKE_CREATE_SUCCESS("좋아요가 생성되었습니다."),
    LIKE_DELETE_SUCCESS("좋아요가 삭제되었습니다."),
    LIKE_COUNT_SUCCESS("좋아요 개수를 조회했습니다."),

    //좌석
    SEAT_GET_AVAILABLE_SUCCESS("예매 가능한 좌석 목록 조회 성공."),
    SEAT_GET_AVAILABLE_TYPE_SUCCESS("좌석 등급별 남은 좌석 수 조회 성공."),

    //금액
    PRICE_SHOW_SEAT_LIST_SUCCESS("공연별 좌석 금액 목록 조회 성공."),


    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
