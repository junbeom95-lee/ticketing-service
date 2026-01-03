package com.chil.ticketingservice.domain.seat.dto;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class SeatCode {
    private final SeatTypeEnum seatTypeEnum;
    private final Integer seatNumber;

    public SeatCode(String seat) {

        if (seat == null || seat.isBlank() || !seat.matches("^(VIP|S|A|B)\\d+$")) {
            throw new CustomException(ExceptionCode.SEAT_BAD_REQUEST);
        }

        this.seatTypeEnum = getSeatTypeEnum(seat);
        this.seatNumber = getSeatNumber(seat);
    }

    //좌석 등급 얻기
    private SeatTypeEnum getSeatTypeEnum(String seat) {
        String seatType = seat.replaceAll("\\d", "");

        return Arrays
                .stream(SeatTypeEnum.values())
                .filter(s -> s.name().equalsIgnoreCase(seatType))
                .findFirst()
                .orElseThrow(
                        () -> new CustomException(ExceptionCode.SEAT_BAD_REQUEST));
    }

    //좌석 번호 얻기
    private Integer getSeatNumber(String seat) {
        String seatNumberString = seat.replaceAll("\\D", "");

        if (seatNumberString.isEmpty()) {
            throw new CustomException(ExceptionCode.SEAT_BAD_REQUEST);
        }

        int seatNumber = Integer.parseInt(seatNumberString);

        if (seatNumber <= 0 || seatNumber > seatTypeEnum.getSize()) {
            throw new CustomException(ExceptionCode.SEAT_BAD_REQUEST);
        }

        return seatNumber;
    }
}
