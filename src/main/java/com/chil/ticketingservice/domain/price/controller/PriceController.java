package com.chil.ticketingservice.domain.price.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.price.dto.request.ShowSeatPriceRegRequest;
import com.chil.ticketingservice.domain.price.dto.response.PriceShowSeatResponse;
import com.chil.ticketingservice.domain.price.dto.response.ShowSeatPriceRegResponse;
import com.chil.ticketingservice.domain.price.service.PriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    // 공연별 좌석 금액 등록 요청/검증 메서드
    @PostMapping("/{showId}/prices")
    public ResponseEntity<CommonResponse<ShowSeatPriceRegResponse>> showSeatPriceReg(
            @PathVariable Long showId,

            @Valid
            @RequestBody ShowSeatPriceRegRequest request
    ) {
        ShowSeatPriceRegResponse result = priceService.showSeatPriceReg(showId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        CommonResponse.success(
                                SuccessMessage.SHOW_CREATE_SEAT_SUCCESS,
                                result
                        )
                );
    }

    //공연별 좌석 금액 목록 조회
    @GetMapping("/{showId}/prices")
    public ResponseEntity<CommonResponse<List<PriceShowSeatResponse>>> getShowSeatPriceList(@PathVariable Long showId) {

        List<PriceShowSeatResponse> priceServiceShowSeatPriceList = priceService.getShowSeatPriceList(showId);

        CommonResponse<List<PriceShowSeatResponse>> response =
                CommonResponse.success(SuccessMessage.PRICE_SHOW_SEAT_LIST_SUCCESS, priceServiceShowSeatPriceList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
