package com.chil.ticketingservice.domain.booking.dto.res;

import com.chil.ticketingservice.domain.booking.entity.Booking;

import java.time.LocalDateTime;

public record BookingResponse(
        Long bookingId,
        Long showId,
        Long userId,
        String seat,
        Boolean paymentStatus,
        LocalDateTime createAt
) {
    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getBookingId(),
                booking.getShow().getId(),
                booking.getUser().getId(),
                booking.getSeat(),
                booking.getPaymentStatus(),
                booking.getCreatedAt()
        );
    }
}
