package com.chil.ticketingservice.common.aspect;

import static com.chil.ticketingservice.common.enums.ExceptionCode.SEAT_ALREADY_BOOKED;

import com.chil.ticketingservice.common.annotation.RedisLock;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.common.redis.RedisLockService;
import com.chil.ticketingservice.domain.booking.dto.request.BookingCreateRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockAspect {

    private final RedisLockService lockService;

    @Around("@annotation(redisLock)")
    public Object run(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {

        Object[] args = joinPoint.getArgs();
        BookingCreateRequest requestDto = (BookingCreateRequest) args[1];

        String key = String.format("lock:showId:%d:seatId:%s", requestDto.showId(), requestDto.seat());
        String value = UUID.randomUUID().toString();

        boolean locked = lockService.tryLock(key, value, 5);

        if (!locked) {
            log.info("락 획득 실패 : {}", Thread.currentThread().getName());
            throw new CustomException(SEAT_ALREADY_BOOKED);
        }

        try {
            log.info("락 획득 성공 : {}", Thread.currentThread().getName());
            return joinPoint.proceed();
        } finally {
            lockService.unlock(key, value);
        }
    }
}
