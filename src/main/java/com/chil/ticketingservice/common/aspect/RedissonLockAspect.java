package com.chil.ticketingservice.common.aspect;

import static com.chil.ticketingservice.common.enums.ExceptionCode.SEAT_ALREADY_BOOKED;

import com.chil.ticketingservice.common.annotation.RedisLock;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.booking.dto.request.BookingCreateRequest;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class RedissonLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {

        Object[] args = joinPoint.getArgs();
        BookingCreateRequest requestDto = (BookingCreateRequest) args[1];

        String lockKey = String.format("lock:showId:%d:seatId:%s", requestDto.showId(), requestDto.seat());
        long waitTime = 0;
        long leaseTime = 5;

        RLock rLock = redissonClient.getLock(lockKey);

        boolean lockAcquired = false;

        try {

            lockAcquired = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

            if (!lockAcquired) {
                log.warn("[RedissonLock] 락 획득 실패 - lockKey: {}", lockKey);
                throw new CustomException(SEAT_ALREADY_BOOKED);
            }

            log.info("[RedissonLock] 락 획득 성공 - lockKey: {}", lockKey);

            return joinPoint.proceed();

        } finally {

            if (lockAcquired && rLock.isHeldByCurrentThread()) {
                try {
                    rLock.unlock();
                    log.info("[RedissonLock] 락 해제 완료 - lockKey: {}", lockKey);
                } catch (IllegalMonitorStateException e) {
                    log.warn("[RedissonLock] 이미 해제된 락 또는 스레드 불일치 - lockKey: {}", lockKey, e);
                }
            }
        }
    }
}
