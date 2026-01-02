package com.chil.ticketingservice.domain.like.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/shows/{showId}/likes/")
    public ResponseEntity<CommonResponse<Void>> postLike(
        // 로그인 유저
        @PathVariable long showId
    ) {
        SuccessMessage successMessage = likeService.postLike(1L, showId);
        return ResponseEntity.ok().body(CommonResponse.successNodata(successMessage));
    }
}
