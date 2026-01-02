package com.chil.ticketingservice.domain.seat.repository;

import com.chil.ticketingservice.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
