package com.chil.ticketingservice.domain.show.repository;

import com.chil.ticketingservice.domain.show.entity.SeatPrice;
import com.chil.ticketingservice.domain.show.enums.SeatTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatPriceRepository extends JpaRepository<SeatPrice, Long> {
    boolean existsByShow_IdAndSeatType(Long showId, SeatTypeEnum seatType);
}
