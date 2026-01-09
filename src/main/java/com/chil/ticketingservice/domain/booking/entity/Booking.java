package com.chil.ticketingservice.domain.booking.entity;

import com.chil.ticketingservice.common.entity.BaseEntity;
import com.chil.ticketingservice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "bookings",
    indexes = {
        @Index(
            name = "idx_booking_expire",
            columnList = "payment_status, is_canceled, created_at"
        )
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long showId;

    @Column(length = 20, nullable = false)
    private String seat;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Boolean paymentStatus;

    @Column(nullable = false)
    private Boolean isCanceled;

    public static Booking createBooking(User user, Long showId, String seat, Integer price) {
        Booking booking = new Booking();
        booking.user = user;
        booking.showId = showId;
        booking.seat = seat;
        booking.price = price;
        booking.paymentStatus = false;
        booking.isCanceled = false;
        return booking;
    }

    public void cancelBooking() {
        this.isCanceled = true;
        this.paymentStatus = false;
    }

    public void processPayment() {
        this.paymentStatus = true;
    }
}
