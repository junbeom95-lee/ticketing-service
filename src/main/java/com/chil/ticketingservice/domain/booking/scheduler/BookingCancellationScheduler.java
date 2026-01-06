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

        LocalDateTime expired = LocalDateTime.now().minusMinutes(10);

        List<Booking> bookingList = bookingRepository.findExpiredBookings(expired);

        bookingList.forEach(booking -> {
            booking.cancelBooking();
            Seat seat = seatRepository.findSeatBySeatCode(booking.getShow(), booking.getSeat());
            seat.availableSeat();
        });

        log.info("{} - 결제 시간 초과로 {}건의 예매 취소 처리", expired, bookingList.size());
    }
}
