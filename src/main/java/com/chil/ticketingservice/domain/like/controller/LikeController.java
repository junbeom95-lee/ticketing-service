package com.chil.ticketingservice.domain.like.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/shows/{showId}/likes")
    public ResponseEntity<CommonResponse<Void>> postLike(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long showId
    ) {
        SuccessMessage response = likeService.postLike(userId, showId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNodata(response));
    }
}
