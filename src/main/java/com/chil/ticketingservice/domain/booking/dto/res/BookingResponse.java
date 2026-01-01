package com.chil.ticketingservice.domain.booking.dto.res;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingResponse {

    private Long bookingId;
    private Long showId;
    private Long userId;
    private String seat;
    private Boolean paymentStatus;
    private LocalDateTime createAt;

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
