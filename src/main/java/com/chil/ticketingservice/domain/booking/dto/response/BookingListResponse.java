package com.chil.ticketingservice.domain.booking.dto.response;

import com.chil.ticketingservice.domain.booking.entity.Booking;

public record BookingListResponse(
        Long bookingId,
        Long showId,
        String seat,
        Boolean paymentStatus,
        Boolean isCanceled
) {
    public static BookingListResponse from(Booking booking) {
        return new BookingListResponse(
                booking.getBookingId(),
                booking.getShow().getId(),
                booking.getSeat(),
                booking.getPaymentStatus(),
                booking.getIsCanceled()
        );
    }
}
