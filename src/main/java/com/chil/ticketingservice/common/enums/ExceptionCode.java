package com.chil.ticketingservice.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    //유저
    EMAIL_EXIST(HttpStatus.CONFLICT, "이메일이 존재합니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    //공연
    SHOW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공연 ID"),


    //좌석 금액


    //좋아요


    //예매
    SEAT_ALREADY_BOOKED(HttpStatus.CONFLICT, "이미 예약된 자석"),
    BOOKING_PRICE_MISMATCH(HttpStatus.CONFLICT, "좌석 가격이 일치하지 않습니다")


    ;

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
