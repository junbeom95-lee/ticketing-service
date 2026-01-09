package com.chil.ticketingservice.domain.booking.dto.response;

import com.chil.ticketingservice.domain.booking.entity.Booking;

public record BookingPaymentResponse(
        Long bookingId,
        Boolean paymentStatus
) {
    public static BookingPaymentResponse from(Booking booking) {
        return new BookingPaymentResponse(
                booking.getBookingId(),
                booking.getPaymentStatus()
        );
    }
}
