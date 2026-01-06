package com.chil.ticketingservice.domain.booking.scheduler;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import com.chil.ticketingservice.domain.booking.repository.BookingRepository;
import com.chil.ticketingservice.domain.seat.entity.Seat;
import com.chil.ticketingservice.domain.seat.repository.SeatRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class BookingCancellationScheduler {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void cancelBooking () {

        int cancelledBookings = bookingRepository.cancelExpiredBookings();

        log.info("{} - 만료 처리된 예매/좌석 row 수: {}", LocalDateTime.now(), cancelledBookings);
    }
}
