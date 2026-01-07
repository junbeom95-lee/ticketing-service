package com.chil.ticketingservice.domain.booking.scheduler;

import static com.chil.ticketingservice.common.aspect.SchedulerEventLogAspect.LAST_BOOKING_TIME;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import com.chil.ticketingservice.domain.booking.repository.BookingRepository;
import com.chil.ticketingservice.domain.booking.service.BookingCancellationBatchService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j(topic = "BookingCancellationScheduler")
public class BookingCancellationScheduler {

    private final BookingRepository bookingRepository;
    private final StringRedisTemplate redisTemplate;
    private final BookingCancellationBatchService bookingCancellationBatchService;

    private static final int BATCH_SIZE = 500;

    @Scheduled(fixedDelay = 60000)
    public void cancelBooking() {

        String value = redisTemplate.opsForValue().get(LAST_BOOKING_TIME);
        LocalDateTime lastBookedDateTime =
            value == null ? null :
                LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(value)), ZoneId.systemDefault());

        if (lastBookedDateTime == null ||
            lastBookedDateTime.isAfter(LocalDateTime.now().minusMinutes(10))) {
            return;
        }

        LocalDateTime expiredAt = LocalDateTime.now().minusMinutes(10);
        Pageable pageable = PageRequest.of(0, BATCH_SIZE);

        while (true) {

            Page<Booking> page = bookingRepository.findExpiredBookings(expiredAt, pageable);

            if (page.isEmpty()) {
                break;
            }

            bookingCancellationBatchService.processCancellation(page.getContent());

            log.info("{} - 만료 처리된 예매/좌석 row 수: {}", LocalDateTime.now(), page.getContent().size());

            pageable = PageRequest.of(0, BATCH_SIZE);
        }
    }
}
