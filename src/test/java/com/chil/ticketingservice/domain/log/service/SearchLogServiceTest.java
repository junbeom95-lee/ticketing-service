package com.chil.ticketingservice.domain.log.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.chil.ticketingservice.domain.log.entity.SearchLog;
import com.chil.ticketingservice.domain.log.repository.SearchLogRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SearchLogServiceTest {

    @Autowired
    private SearchLogService searchLogService;

    @Autowired
    private SearchLogRepository searchLogRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("검색 로그 저장 성공 - 첫 검색")
    void saveSearchLog_success_firstSearch() {
        // Given
        Long userId = 1L;
        String showTitle = "TestShow-" + UUID.randomUUID();
        LocalDateTime searchTime = LocalDateTime.now();

        // When
        boolean result = searchLogService.saveSearchLog(userId, showTitle, searchTime);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("검색 로그 저장 실패 - 같은 날 중복 검색")
    void saveSearchLog_fail_duplicateSearchSameDay() {
        // Given
        Long userId = 2L;
        String showTitle = "TestShow-" + UUID.randomUUID();
        LocalDateTime searchTime = LocalDateTime.now();

        // 첫 번째 검색 저장
        searchLogService.saveSearchLog(userId, showTitle, searchTime);

        // When - 같은 날 같은 공연 재검색
        LocalDateTime secondSearchTime = searchTime.plusHours(3);
        boolean result = searchLogService.saveSearchLog(userId, showTitle, secondSearchTime);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("검색 로그 저장 성공 - 다른 날 검색")
    void saveSearchLog_success_differentDay() {
        // Given
        Long userId = 3L;
        String showTitle = "TestShow-" + UUID.randomUUID();
        LocalDateTime searchTime = LocalDateTime.now();

        // 첫 번째 검색 저장
        searchLogService.saveSearchLog(userId, showTitle, searchTime);

        // When - 다음날 같은 공연 검색
        LocalDateTime nextDaySearchTime = searchTime.plusDays(1);
        boolean result = searchLogService.saveSearchLog(userId, showTitle, nextDaySearchTime);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("검색 로그 저장 성공 - 다른 유저가 같은 공연 검색")
    void saveSearchLog_success_differentUser() {
        // Given
        Long userId1 = 4L;
        Long userId2 = 5L;
        String showTitle = "TestShow-" + UUID.randomUUID();
        LocalDateTime searchTime = LocalDateTime.now();

        // 첫 번째 유저 검색 저장
        searchLogService.saveSearchLog(userId1, showTitle, searchTime);

        // When - 다른 유저가 같은 공연 검색
        boolean result = searchLogService.saveSearchLog(userId2, showTitle, searchTime);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("검색 로그 저장 성공 - 같은 유저가 다른 공연 검색")
    void saveSearchLog_success_differentShow() {
        // Given
        Long userId = 6L;
        String showTitle1 = "TestShow1-" + UUID.randomUUID();
        String showTitle2 = "TestShow2-" + UUID.randomUUID();
        LocalDateTime searchTime = LocalDateTime.now();

        // 첫 번째 공연 검색 저장
        searchLogService.saveSearchLog(userId, showTitle1, searchTime);

        // When - 같은 유저가 다른 공연 검색
        boolean result = searchLogService.saveSearchLog(userId, showTitle2, searchTime);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("검색 로그 삭제 성공 - 1달 지난 로그만 삭제")
    void deleteSearchLog_success() {
        // Given
        Long userId = 7L;
        String showTitle = "Test Show Delete";

        // 2달 전 로그
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);
        searchLogRepository.save(new SearchLog(userId, showTitle + "1", twoMonthsAgo));

        // 1.5달 전 로그
        LocalDateTime oneAndHalfMonthAgo = LocalDateTime.now().minusMonths(1).minusDays(15);
        searchLogRepository.save(new SearchLog(userId, showTitle + "2", oneAndHalfMonthAgo));

        // 15일 전 로그
        LocalDateTime fifteenDaysAgo = LocalDateTime.now().minusDays(15);
        searchLogRepository.save(new SearchLog(userId, showTitle + "3", fifteenDaysAgo));

        // 오늘 로그
        LocalDateTime today = LocalDateTime.now();
        searchLogRepository.save(new SearchLog(userId, showTitle + "4", today));

        // When
        int deletedCount = searchLogService.deleteSearchLog();

        // Then
        assertThat(deletedCount).isEqualTo(2); // 2달 전, 1.5달 전 로그 삭제
    }

    @Test
    @DisplayName("검색 로그 삭제 - 삭제할 로그가 없음")
    void deleteSearchLog_noLogsToDelete() {
        // Given
        Long userId = 8L;
        String showTitle = "Test Show No Delete";

        // 최근 로그만 저장
        LocalDateTime today = LocalDateTime.now();
        searchLogRepository.save(new SearchLog(userId, showTitle, today));

        // When
        int deletedCount = searchLogService.deleteSearchLog();

        // Then
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    @DisplayName("검색 로그 삭제 - 모든 로그 삭제")
    void deleteSearchLog_deleteAllLogs() {
        // Given
        Long userId = 9L;
        String showTitle = "Test Show Delete All";

        // 모두 1달 이상 지난 로그
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);
        searchLogRepository.save(new SearchLog(userId, showTitle + "1", twoMonthsAgo));

        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        searchLogRepository.save(new SearchLog(userId, showTitle + "2", threeMonthsAgo));

        // When
        int deletedCount = searchLogService.deleteSearchLog();

        // Then
        assertThat(deletedCount).isEqualTo(2);
    }
}
