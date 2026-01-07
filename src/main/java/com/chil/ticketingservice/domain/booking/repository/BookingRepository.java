package com.chil.ticketingservice.domain.booking.repository;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // findAllByUser_Id: 특정 회원의 모든 예매 조회용 메서드 (페이징)
    Page<Booking> findAllByUser_Id(Long userId, Pageable pageable);

    List<Booking> findAllByShowId(Long showId);

    // 예매 후 10분 이내에 결제 되지 않은 예매 조회
    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.paymentStatus = false
          AND b.isCanceled = false
          AND b.createdAt <= :expiredAt
        """)
    Page<Booking> findExpiredBookings(@Param("expiredAt") LocalDateTime expiredAt, Pageable pageable);
}
