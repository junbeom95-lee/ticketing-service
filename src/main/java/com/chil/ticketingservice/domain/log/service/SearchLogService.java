package com.chil.ticketingservice.domain.log.service;

import com.chil.ticketingservice.domain.log.entity.SearchLog;
import com.chil.ticketingservice.domain.log.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean saveSearchLog(Long userId, String showTitle, LocalDateTime searchTime){
        LocalDateTime startOfDay = searchTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = searchTime.toLocalDate().atTime(23, 59, 59);

        boolean searchLog = searchLogRepository.existsByUserIdAndShowTitleAndSearchTimeBetween(userId, showTitle, startOfDay, endOfDay);

        if (searchLog) {
            return false;
        }

        searchLogRepository.save(new SearchLog(userId, showTitle, searchTime));

        return true;
    }
}
