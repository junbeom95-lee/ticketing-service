package com.chil.ticketingservice.domain.booking.repository;

import com.chil.ticketingservice.domain.booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // findByShowIdAndSeatAndIsCanceledFalse: 취소되지 않은 예매의 좌석 중복 확인용 메서드
    Optional<Booking> findByShowIdAndSeatAndIsCanceledFalse(Long showId, String seat);

    // findAllByUser_Id: 특정 회원의 모든 예매 조회용 메서드 (페이징)
    Page<Booking> findAllByUser_Id(Long userId, Pageable pageable);

    // 예매 후 10분 이내에 결제 되지 않은 예매 조회
    @Modifying
    @Transactional
    @Query(value = """
        UPDATE bookings b
            JOIN seats s
            ON b.show_id = s.show_id
                AND s.seat_type = REGEXP_SUBSTR(b.seat, '^[A-Z]+')
                AND s.seat_number = CAST(REGEXP_SUBSTR(b.seat, '[0-9]+$') AS UNSIGNED)
        SET
            b.is_canceled = 1,
            s.seat_status = 1
        WHERE
            b.payment_status = 0
          AND b.is_canceled = 0
          AND b.created_at <= NOW() - INTERVAL 10 MINUTE
        """, nativeQuery = true)
    int cancelExpiredBookings();
}
