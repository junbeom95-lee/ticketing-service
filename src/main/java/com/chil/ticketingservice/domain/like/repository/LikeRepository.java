package com.chil.ticketingservice.domain.like.repository;

import com.chil.ticketingservice.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserIdAndShowId(long userId, long showId);

    void deleteByUserIdAndShowId(long userId, long showId);
}
