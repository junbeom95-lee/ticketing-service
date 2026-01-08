package com.chil.ticketingservice.domain.seat.repository;

import com.chil.ticketingservice.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SeatBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<Seat> seatList) {

        String sql = "insert into seats (show_id, seat_type, seat_number, seat_status) values (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, seatList, 1000,
                (ps, seat) -> {
                    ps.setLong(1, seat.getShow().getId());
                    ps.setString(2, seat.getSeatType().name());
                    ps.setInt(3, seat.getSeatNumber());
                    ps.setBoolean(4, seat.getSeatStatus());
                });

        log.info("SeatBatchRepository batch 완료");
    }
}
