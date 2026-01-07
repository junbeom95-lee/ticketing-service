package com.chil.ticketingservice.domain.log.repository;

import com.chil.ticketingservice.domain.log.entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
    boolean existsByUserIdAndShowTitleAndSearchTimeBetween(Long userId, String showTitle, LocalDateTime startOfDay, LocalDateTime endOfDay);

    // 한 달 지난 로그 삭제 쿼리
    @Modifying // 삭제시 꼭 필요!
    @Query("DELETE FROM SearchLog s WHERE s.searchTime = :datetime")
    int deleteBySearchTime(@Param("datetime") LocalDateTime datetime);
}
