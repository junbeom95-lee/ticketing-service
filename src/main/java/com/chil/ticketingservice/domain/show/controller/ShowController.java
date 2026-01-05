package com.chil.ticketingservice.domain.show.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import com.chil.ticketingservice.domain.show.dto.response.ShowCreateResponse;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.chil.ticketingservice.domain.show.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    // 공연 생성 요청/검증 메서드
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<ShowCreateResponse>> createShow(
            @RequestPart("request") @Valid ShowCreateRequest request,
            @RequestPart("image") MultipartFile image
    ) {
        ShowCreateResponse result = showService.createShow(request, image);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(
                        SuccessMessage.SHOW_CREATE_SUCCESS,
                        result
                ));
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
    // 최신순: latest
    // 공연 임박순: upcoming
    // 판매 많은순: bestseller
    // 인기순: popular
    @GetMapping
    public ResponseEntity<CommonResponse<Page<ShowResponse>>> showList(
            @RequestParam(defaultValue = "latest") String keyword,
            Pageable pageable
    ) {
        Page<ShowResponse> result = showService.showList(keyword, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        CommonResponse.success(
                                SuccessMessage.SHOW_RESPONSE_SUCCESS,
                                result
                        )
                );
    }

    // 공연 상세 조회 요청/검증 메서드
    @GetMapping("/{showId}")
    public ResponseEntity<CommonResponse<ShowResponse>> showDetail (
        @PathVariable Long showId
    ) {
        ShowResponse result = showService.showDetail(showId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        CommonResponse.success(
                                SuccessMessage.SHOW_RESPONSE_SUCCESS,
                                result
                        )
                );
    }
}
