package com.chil.ticketingservice.domain.price.enums;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SeatTypeEnum {

    VIP, S, A, B;

    public static SeatTypeEnum of(String value){
        return Arrays
                .stream(SeatTypeEnum.values())
                .filter(s -> s.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(
                        () -> new CustomException(ExceptionCode.SHOW_INVALID_SEAT_TYPE)
                );
    }
}
