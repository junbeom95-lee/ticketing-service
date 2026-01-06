package com.chil.ticketingservice.domain.booking.repository;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // findAllByUser_Id: 특정 회원의 모든 예매 조회용 메서드 (페이징)
    Page<Booking> findAllByUser_Id(Long userId, Pageable pageable);

    List<Booking> findAllByShowId(Long showId);
}
