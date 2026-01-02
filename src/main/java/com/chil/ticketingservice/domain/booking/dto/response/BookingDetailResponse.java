package com.chil.ticketingservice.domain.booking.dto.response;

import com.chil.ticketingservice.domain.booking.entity.Booking;

import java.time.LocalDateTime;

public record BookingDetailResponse(
        Long bookingId,
        Long showId,
        Long userId,
        String seat,
        Boolean paymentStatus,
        Boolean isCanceled,
        LocalDateTime createAt,
        LocalDateTime modifiedAt
) {
    public static BookingDetailResponse from(Booking booking) {
        return new BookingDetailResponse(
                booking.getBookingId(),
                booking.getShow().getId(),
                booking.getUser().getId(),
                booking.getSeat(),
                booking.getPaymentStatus(),
                booking.getIsCanceled(),
                booking.getCreatedAt(),
                booking.getModifiedAt()
        );
    }
}
