package com.chil.ticketingservice.domain.search.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.search.dto.request.SearchCreateRequest;
import com.chil.ticketingservice.domain.search.service.SearchService;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<Page<ShowResponse>>> totalSearch(
            @Valid
            @RequestBody SearchCreateRequest request,

            @AuthenticationPrincipal Long userId,
            Pageable pageable
    ) {
        Page<ShowResponse> result = searchService.totalSearch(request, userId, pageable);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        CommonResponse.success(
                                SuccessMessage.SHOW_RESPONSE_SUCCESS,
                                result
                        )
                );
    }
}
