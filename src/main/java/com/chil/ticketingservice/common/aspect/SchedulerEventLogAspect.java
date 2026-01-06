package com.chil.ticketingservice.common.aspect;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerEventLogAspect {

    private final RedisTemplate<String, Object> redisTemplate;
    public static final String CACHE_PREFIX = "last_booking_time";

    @AfterReturning("execution(* com.chil.ticketingservice.domain.booking.service.BookingService.createBooking(..))")
    public void setLastCreatedTime() {
        redisTemplate.opsForValue().set(CACHE_PREFIX, LocalDateTime.now().toString());
    }
}
