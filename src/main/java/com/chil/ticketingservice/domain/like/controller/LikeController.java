package com.chil.ticketingservice.domain.like.controller;

import static com.chil.ticketingservice.common.enums.SuccessMessage.LIKE_COUNT_SUCCESS;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.like.dto.response.LikeCountResponse;
import com.chil.ticketingservice.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요 생성/삭제
    @PostMapping("/shows/{showId}/likes")
    public ResponseEntity<CommonResponse<Void>> postLike(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long showId
    ) {
        SuccessMessage response = likeService.postLike(userId, showId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNodata(response));
    }

    // 공연별 좋아요 수 조회
    @GetMapping("/shows/{showId}/likes")
    public ResponseEntity<CommonResponse<LikeCountResponse>> countLikes(
        @PathVariable Long showId
    ) {
        LikeCountResponse response = likeService.countLikes(showId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(LIKE_COUNT_SUCCESS, response));
    }
}
