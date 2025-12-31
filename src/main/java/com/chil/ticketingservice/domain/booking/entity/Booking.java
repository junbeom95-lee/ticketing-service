package com.chil.ticketingservice.domain.booking.entity;

import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "bookings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(length = 20, nullable = false, unique = true)
    private String seat;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Boolean paymentStatus;
    
    @Column(nullable = false)
    private Boolean isCanceled;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        paymentStatus = false;
        isCanceled = false;
    }
}
