package com.chil.ticketingservice.domain.booking.service;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import com.chil.ticketingservice.domain.booking.repository.BookingRepository;
import com.chil.ticketingservice.domain.seat.entity.Seat;
import com.chil.ticketingservice.domain.seat.repository.SeatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingCancellationBatchService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public void processCancellation(List<Booking> bookings) {

        for (Booking booking : bookings) {

            booking.cancelBooking();

            Seat seat = seatRepository.findSeatBySeatCode(booking.getShow(), booking.getSeat());
            seat.availableSeat();
        }

        bookingRepository.flush();
        seatRepository.flush();
    }
}
