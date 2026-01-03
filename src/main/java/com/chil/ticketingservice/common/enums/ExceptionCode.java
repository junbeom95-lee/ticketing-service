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
    SHOW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공연 ID 입니다."),
    SHOW_INVALID_SEAT_TYPE(HttpStatus.BAD_REQUEST, "좌석 타입은 VIP, S, A, B 중 하나여야 합니다."),
    SHOW_SEAT_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 좌석입니다."),


    //좌석 금액
    SEAT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "올바르지 않은 좌석입니다."),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "좌석이 존재하지 않습니다."),


    //좋아요


    //예매
    SEAT_ALREADY_BOOKED(HttpStatus.CONFLICT, "이미 예약된 자석."),
    BOOKING_PRICE_MISMATCH(HttpStatus.CONFLICT, "좌석 가격이 일치하지 않습니다."),
    BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예매입니다."),
    BOOKING_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 예매입니다."),
    BOOKING_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 예매에 대한 접근 권한이 없습니다.")


    ;

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
