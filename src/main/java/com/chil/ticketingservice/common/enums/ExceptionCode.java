package com.chil.ticketingservice.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    //유저
    EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 존재합니다."),


    //공연
    SHOW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공연 ID 입니다.")

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
