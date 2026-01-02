package com.chil.ticketingservice.domain.price.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.price.dto.request.ShowSeatPriceRegRequest;
import com.chil.ticketingservice.domain.price.dto.response.ShowSeatPriceRegResponse;
import com.chil.ticketingservice.domain.price.service.PriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        CommonResponse.success(
                                SuccessMessage.SHOW_CREATE_SEAT_SUCCESS,
                                priceService.showSeatPriceReg(showId, request)
                        )
                );
    }
}
