package com.chil.ticketingservice.domain.seat.repository;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableResponse;
import com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableTypeResponse;
import com.chil.ticketingservice.domain.seat.entity.Seat;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("""
        select new com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableResponse(concat(s.seatType, s.seatNumber))
        from Seat s
        where s.show = :show 
        and s.seatType = :seatType
        and s.seatStatus = true
        """)
    List<SeatAvailableResponse> findAllByShowAndSeatTypeAndSeatStatus(@Param("show") Show show, @Param("seatType")SeatTypeEnum seatType);

    @Query("""
        select new com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableTypeResponse(
                s.seatType, count(s.seatNumber)
                )
        from Seat s
        where s.show = :show
        and s.seatStatus = true
        group by s.seatType
        """)
    List<SeatAvailableTypeResponse> countByShow(@Param("show") Show show);


    Optional<Seat> findByShowAndSeatTypeAndSeatNumber(Show show, SeatTypeEnum seatType, Integer seatNumber);

    //좌석 예매 가능 확인
    default void checkSeatAvailable(Show show, SeatTypeEnum seatType, Integer seatNumber) {
        Seat seat = findByShowAndSeatTypeAndSeatNumber(show, seatType, seatNumber).orElseThrow(
                () -> new CustomException(ExceptionCode.SEAT_NOT_FOUND));

        if (!seat.getSeatStatus()) {
            throw new CustomException(ExceptionCode.SEAT_ALREADY_BOOKED);
        }
    }
}
