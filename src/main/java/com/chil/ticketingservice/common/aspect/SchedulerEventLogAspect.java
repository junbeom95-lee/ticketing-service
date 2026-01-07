package com.chil.ticketingservice.common.aspect;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerEventLogAspect {

    private final StringRedisTemplate redisTemplate;
    public static final String LAST_BOOKING_TIME = "last_booking_time";

    @AfterReturning("execution(* com.chil.ticketingservice.domain.booking.service.BookingService.createBooking(..))")
    public void setLastBookingTime() {
        redisTemplate.opsForValue().set(
            LAST_BOOKING_TIME,
            String.valueOf(System.currentTimeMillis()),
            15, TimeUnit.MINUTES);
    }
}
