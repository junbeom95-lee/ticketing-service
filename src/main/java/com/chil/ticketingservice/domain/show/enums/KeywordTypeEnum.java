package com.chil.ticketingservice.domain.show.enums;

import lombok.Getter;

@Getter
public enum KeywordTypeEnum {
    UPCOMING,
    BESTSELLER,
    POPULAR,
    LATEST
    ;

    // 소문자로 변환
    public static KeywordTypeEnum from(String value) {
        return KeywordTypeEnum.valueOf(value.toUpperCase());
    }
}