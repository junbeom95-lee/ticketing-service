package com.chil.ticketingservice.domain.booking.dto.response;

import com.chil.ticketingservice.domain.booking.entity.Booking;

public record BookingResponse(
        Long bookingId,
        Long showId,
        Long userId,
        String seat,
        Boolean paymentStatus,
        Boolean isCanceled
) {
    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getBookingId(),
                booking.getShow().getId(),
                booking.getUser().getId(),
                booking.getSeat(),
                booking.getPaymentStatus(),
                booking.getIsCanceled()
        );
    }
}
