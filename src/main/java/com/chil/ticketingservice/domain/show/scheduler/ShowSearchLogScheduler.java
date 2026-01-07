package com.chil.ticketingservice.domain.show.scheduler;

import com.chil.ticketingservice.domain.log.service.SearchLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShowSearchLogScheduler {

    private final SearchLogService searchLogService;

    // 매달 1일 자정 삭제
    @Scheduled(cron = "0 0 0 1  * *")
    public void SearchLogScheduled() {
        int deleteRow = searchLogService.deleteSearchLog();

        log.info("삭제된 행: {}", deleteRow);
    }
}
