package com.chil.ticketingservice.domain.show.service;

import com.chil.ticketingservice.domain.log.service.SearchLogService;
import com.chil.ticketingservice.domain.show.dto.response.SearchRankResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

            if (searchLog) {
                stringRedisTemplate.opsForZSet().incrementScore(rankKey, title, 1);
            } else {
                log.info("저장돼 있는 userId 와 ShowTitle 입니다: {}, {}", userId, title);
            }

            Long expire = stringRedisTemplate.getExpire(rankKey, TimeUnit.SECONDS);
            // 자정이 되면 rankKey 제거
            // expire == -1 -> TTL 없음
            if (expire == null || expire == -1) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay(); // 자정 시간 만들기
                long seconds = Duration.between(now, midnight).getSeconds(); // 지금부터 자정까지 몇 초 남았는지 계산

                // 초단위로 계산(TimeUnit.SECONDS)
                stringRedisTemplate.expire(rankKey, seconds, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.warn("Redis 검색 로그 저장 실패", e);
        }
    }

    public List<SearchRankResponse> searchRankList(int limit) {
        String rankKey = SEARCH_RANK + LocalDate.now();

        Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet()
                .reverseRangeWithScores(rankKey, 0, limit - 1);

        if (result == null || result.isEmpty()) {
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
