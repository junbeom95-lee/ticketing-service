package com.chil.ticketingservice.domain.booking.dto.response;

import com.chil.ticketingservice.domain.booking.entity.Booking;

import java.time.LocalDateTime;

public record BookingCreateResponse(
        Long bookingId,
        Long showId,
        Long userId,
        String seat,
        Boolean paymentStatus,
        LocalDateTime createAt
) {
    public static BookingCreateResponse from(Booking booking) {
        return new BookingCreateResponse(
                booking.getBookingId(),
                booking.getShow().getId(),
                booking.getUser().getId(),
                booking.getSeat(),
                booking.getPaymentStatus(),
                booking.getCreatedAt()
        );
    }
}
