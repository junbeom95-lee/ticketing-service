package com.chil.ticketingservice.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    //유저
    EMAIL_EXIST(HttpStatus.CONFLICT, "이메일이 존재합니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다.")

    //공연


    //좌석 금액


    //좋아요


    //예매


    ;

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
