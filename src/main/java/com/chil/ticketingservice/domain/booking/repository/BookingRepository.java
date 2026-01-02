package com.chil.ticketingservice.domain.booking.repository;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // findByShowIdAndSeatAndIsCanceledFalse: 취소되지 않은 예매의 좌석 중복 확인용 메서드
    Optional<Booking> findByShowIdAndSeatAndIsCanceledFalse(Long showId, String seat);
}
