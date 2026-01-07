package com.chil.ticketingservice.domain.show.service;

import com.chil.ticketingservice.domain.log.entity.SearchLog;
import com.chil.ticketingservice.domain.log.repository.SearchLogRepository;
import com.chil.ticketingservice.domain.log.service.SearchLogService;
import com.chil.ticketingservice.domain.show.dto.request.ShowSearchLogRequest;
import com.chil.ticketingservice.domain.show.dto.response.SearchRankResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowSearchRedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private final SearchLogService searchLogService;

    private final static String SEARCH_RANK = "search:rank:";

    public void searchLogSave(Long userId, String title) {
        try {
            LocalDate today = LocalDate.now();

            // 제목 검색 집계 저장
            String rankKey = SEARCH_RANK + today;

           boolean searchLog = searchLogService.saveSearchLog(userId, title, LocalDateTime.now());

            if (searchLog == true) {
                stringRedisTemplate.opsForZSet().incrementScore(rankKey, title, 1);
            } else {
                log.info("저장돼 있는 userId 와 ShowTitle 입니다: {}, {}", userId, title);
            }
        } catch (Exception e) {
            log.warn("Redis 검색 로그 저장 실패", e);
        }
    }

    public List<SearchRankResponse> searchRankList(int limit) {
        String rankKey = SEARCH_RANK + LocalDate.now();

        Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet()
                .reverseRangeWithScores(rankKey, 0, limit - 1);

        if (result.isEmpty() || result == null) {
            return List.of();
        }

        return result
                .stream()
                .map(t -> new SearchRankResponse(
                        t.getValue(),
                        t.getScore().intValue()
                ))
                .toList();
    }
}
