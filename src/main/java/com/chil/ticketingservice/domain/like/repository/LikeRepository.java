package com.chil.ticketingservice.domain.like.repository;

import com.chil.ticketingservice.domain.like.entity.Like;
import com.chil.ticketingservice.domain.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserIdAndShowId(Long userId, Long showId);

    void deleteByUserIdAndShowId(Long userId, Long showId);

    long countByShow(Show show);
}
