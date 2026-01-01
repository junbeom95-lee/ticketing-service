package com.chil.ticketingservice.domain.show.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import com.chil.ticketingservice.domain.show.dto.response.ShowCreateResponse;
import com.chil.ticketingservice.domain.show.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    // 공연 생성 요청/검증 메서드
    @PostMapping("")
    public ResponseEntity<CommonResponse<ShowCreateResponse>> createShow(
            @Valid @RequestBody ShowCreateRequest request
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
}
