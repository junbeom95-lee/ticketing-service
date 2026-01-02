package com.chil.ticketingservice.domain.booking.dto.response;

import com.chil.ticketingservice.domain.booking.entity.Booking;

public record BookingCreateResponse(
        Long bookingId,
        Long showId,
        Long userId,
        String seat,
        Integer price,
        Boolean paymentStatus
) {
    public static BookingCreateResponse from(Booking booking) {
        return new BookingCreateResponse(
                booking.getBookingId(),
                booking.getShow().getId(),
                booking.getUser().getId(),
                booking.getSeat(),
                booking.getPrice(),
                booking.getPaymentStatus()
        );
    }
}
