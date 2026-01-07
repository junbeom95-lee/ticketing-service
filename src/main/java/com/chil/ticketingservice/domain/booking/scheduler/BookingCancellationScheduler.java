package com.chil.ticketingservice.domain.booking.scheduler;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import com.chil.ticketingservice.domain.booking.repository.BookingRepository;
import com.chil.ticketingservice.domain.booking.service.BookingCancellationBatchService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j(topic = "BookingCancellationScheduler")
public class BookingCancellationScheduler {

    private final BookingRepository bookingRepository;
    private final BookingCancellationBatchService bookingCancellationBatchService;

    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void cancelBooking() {

        LocalDateTime standardTime = LocalDateTime.now().minusMinutes(10);

        List<Booking> expiredBookingList = bookingRepository.findExpiredBookings(standardTime);

        if (expiredBookingList.isEmpty()) {
            return;
        }

        bookingCancellationBatchService.processCancellation(expiredBookingList);

        log.info("{} - 만료 처리된 예매/좌석 row 수: {}", LocalDateTime.now(), expiredBookingList.size());
    }
}
