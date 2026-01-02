package com.chil.ticketingservice.domain.seat.repository;

import com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableResponse;
import com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableTypeResponse;
import com.chil.ticketingservice.domain.seat.entity.Seat;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("""
        select new com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableResponse(concat(s.seatType, s.seatNumber))
        from Seat s
        where s.show = :show and s.seatType = :seatType
        """)
    List<SeatAvailableResponse> findAllByShowAndSeatType(@Param("show") Show show, @Param("seatType")SeatTypeEnum seatType);

    @Query("""
        select new com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableTypeResponse(
                s.seatType, count(s.seatNumber)
                )
        from Seat s
        where s.show = :show
        group by s.seatType
        """)
    List<SeatAvailableTypeResponse> findCountByShowId(@Param("show") Show show);
}
