package com.chil.ticketingservice.domain.seat.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableResponse;
import com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableTypeResponse;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/shows")
public class SeatController {

    private final SeatService seatService;

    //예매 가능한 좌석 목록 조회
    @GetMapping("/{showId}/seats/{seatType}")
    public ResponseEntity<CommonResponse<List<SeatAvailableResponse>>> getAvailableSeatList(@PathVariable Long showId,
                                                                                            @PathVariable SeatTypeEnum seatType) {

        List<SeatAvailableResponse> availableResponseList = seatService.getAvailableSeatList(showId, seatType);

        CommonResponse<List<SeatAvailableResponse>> response = CommonResponse.success(SuccessMessage.SEAT_GET_AVAILABLE_SUCCESS, availableResponseList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //좌석 등급별 남은 좌석 수 조회
    @GetMapping("/{showId}/seats")
    public ResponseEntity<CommonResponse<List<SeatAvailableTypeResponse>>> getAvailableSeatTypeList(@PathVariable Long showId) {

        List<SeatAvailableTypeResponse> availableTypeResponseList = seatService.getAvailableSeatTypeList(showId);

        CommonResponse<List<SeatAvailableTypeResponse>> response = CommonResponse.success(SuccessMessage.SEAT_GET_AVAILABLE_TYPE_SUCCESS, availableTypeResponseList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
