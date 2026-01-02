package com.chil.ticketingservice.domain.price.repository;

import com.chil.ticketingservice.domain.price.entity.Price;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {

    boolean existsByShow_IdAndSeatType(Long showId, SeatTypeEnum seatType);

    List<Price> findByShow_Id(Long showId);

    List<Price> findByShow(Show show);
}
