package com.chil.ticketingservice.domain.booking.repository;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // findByShowIdAndSeatAndIsCanceledFalse: 취소되지 않은 예매의 좌석 중복 확인용 메서드
    Optional<Booking> findByShowIdAndSeatAndIsCanceledFalse(Long showId, String seat);

    // findAllByUser_Id: 특정 회원의 모든 예매 조회용 메서드 (페이징)
    Page<Booking> findAllByUser_Id(Long userId, Pageable pageable);
}
