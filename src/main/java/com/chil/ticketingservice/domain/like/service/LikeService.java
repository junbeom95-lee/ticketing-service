package com.chil.ticketingservice.domain.like.service;

import static com.chil.ticketingservice.common.enums.SuccessMessage.LIKE_DELETE_SUCCESS;
import static com.chil.ticketingservice.common.enums.SuccessMessage.LIKE_CREATE_SUCCESS;

import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.like.entity.Like;
import com.chil.ticketingservice.domain.like.repository.LikeRepository;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    // 좋아요 생성/삭제
    @Transactional
    public SuccessMessage postLike(long userId, long showId) {

        // TODO: 로그인 정보로 유저 객체 생성
        User user;
        // TODO: showId로 show 객체 생성
        Show show;

        boolean exist = likeRepository.existsByUserIdAndShowId(userId, showId);

        if (exist) {

            likeRepository.deleteByUserIdAndShowId(userId, showId);

            return LIKE_DELETE_SUCCESS;
        }

        Like like = new Like(show, user);
        likeRepository.save(like);

        return LIKE_CREATE_SUCCESS;
    }
}
