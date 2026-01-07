package com.chil.ticketingservice.domain.booking.service;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import com.chil.ticketingservice.domain.seat.entity.Seat;
import com.chil.ticketingservice.domain.seat.repository.SeatRepository;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingCancellationBatchService {

    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;

    @Transactional
    public void processCancellation(List<Booking> expiredBookingList) {

        for (Booking booking : expiredBookingList) {

            booking.cancelBooking();

            Show show = showRepository.findShowById(booking.getShowId());

            Seat seat = seatRepository.findSeatBySeatCode(show, booking.getSeat());
            seat.availableSeat();
        }
    }
}
