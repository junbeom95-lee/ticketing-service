package com.chil.ticketingservice.domain.booking.scheduler;

import static com.chil.ticketingservice.common.aspect.SchedulerEventLogAspect.CACHE_PREFIX;

import com.chil.ticketingservice.domain.booking.repository.BookingRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j(topic = "BookingCancellationScheduler")
public class BookingCancellationScheduler {

    private final BookingRepository bookingRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void cancelBooking () {

        String value = (String) redisTemplate.opsForValue().get(CACHE_PREFIX);

        LocalDateTime lastBookedDateTime = value == null ? null : LocalDateTime.parse(value);

        if (lastBookedDateTime == null ||
            lastBookedDateTime.isAfter(LocalDateTime.now().minusMinutes(10))) {
            return;
        }

        int cancelledBookings = bookingRepository.cancelExpiredBookings();

        log.info("{} - 만료 처리된 예매/좌석 row 수: {}", LocalDateTime.now(), cancelledBookings);
    }
}
