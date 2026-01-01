package com.chil.ticketingservice.domain.show.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import com.chil.ticketingservice.domain.show.dto.request.ShowSeatPriceRegRequest;
import com.chil.ticketingservice.domain.show.dto.response.ShowCreateResponse;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.chil.ticketingservice.domain.show.dto.response.ShowSeatPriceRegResponse;
import com.chil.ticketingservice.domain.show.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    // 공연 생성 요청/검증 메서드
    @PostMapping("")
    public ResponseEntity<CommonResponse<ShowCreateResponse>> createShow(
            @Valid
            @RequestBody ShowCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        CommonResponse.success(
                                SuccessMessage.SHOW_CREATE_SUCCESS,
                                showService.createShow(request)
                        )
                );
    }

    // 공역 삭제 요청/검증 메서드
    @DeleteMapping("/{showId}")
    public ResponseEntity<Void> deleteShow(
            @PathVariable Long showId
    ) {
        showService.showDelete(showId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    // 공연 조회 리스트 요청/검증 메서드
    @GetMapping("")
    public ResponseEntity<CommonResponse<List<ShowResponse>>> showList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        CommonResponse.success(
                                SuccessMessage.SHOW_RESPONSE_SUCCESS,
                                showService.showList()
                        )
                );
    }

    // 공연 상세 조회 요청/검증 메서드
    @GetMapping("/{showId}")
    public ResponseEntity<CommonResponse<ShowResponse>> showDetail (
        @PathVariable Long showId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        CommonResponse.success(
                                SuccessMessage.SHOW_RESPONSE_SUCCESS,
                                showService.showDetail(showId)
                        )
                );
    }

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
                            showService.showSeatPriceReg(showId, request)
                        )
                );
    }
}
