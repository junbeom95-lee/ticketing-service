package com.chil.ticketingservice.domain.like.service;

import static com.chil.ticketingservice.common.enums.SuccessMessage.LIKE_DELETE_SUCCESS;
import static com.chil.ticketingservice.common.enums.SuccessMessage.LIKE_CREATE_SUCCESS;

import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.like.dto.response.LikeCountResponse;
import com.chil.ticketingservice.domain.like.entity.Like;
import com.chil.ticketingservice.domain.like.repository.LikeRepository;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import com.chil.ticketingservice.domain.user.entity.User;
import com.chil.ticketingservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;

    // 좋아요 생성/삭제
    @Transactional
    public SuccessMessage postLike(Long userId, Long showId) {

        User user = userRepository.findUserById(userId);
        Show show = showRepository.findShowById(showId);

        boolean exist = likeRepository.existsByUserIdAndShowId(userId, showId);

        if (exist) {

            likeRepository.deleteByUserIdAndShowId(userId, showId);

            return LIKE_DELETE_SUCCESS;
        }

        Like like = new Like(show, user);
        likeRepository.save(like);

        return LIKE_CREATE_SUCCESS;
    }

    // 공연별 좋아요 수 조회
    @Transactional(readOnly = true)
    public LikeCountResponse countLikes(long showId) {

        Show show = showRepository.findShowById(showId);

        long likes = likeRepository.countByShow(show);

        return new LikeCountResponse(likes);
    }
}
