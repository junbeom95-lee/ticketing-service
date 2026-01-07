package com.chil.ticketingservice.domain.log.repository;

import com.chil.ticketingservice.domain.log.entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
    boolean existsByUserIdAndShowTitleAndSearchTimeBetween(Long userId, String showTitle, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
