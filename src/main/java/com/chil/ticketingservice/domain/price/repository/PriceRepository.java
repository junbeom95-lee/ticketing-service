package com.chil.ticketingservice.domain.price.repository;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.price.entity.Price;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {

    boolean existsByShow_IdAndSeatType(Long showId, SeatTypeEnum seatType);

    List<Price> findByShow_Id(Long showId);

    List<Price> findByShow(Show show);

    Optional<Price> findByShowAndSeatType(Show show, SeatTypeEnum seatType);

    default Price findWithShowAndSeatType(Show show, SeatTypeEnum seatType) {
        return findByShowAndSeatType(show, seatType).orElseThrow(
                () -> new CustomException(ExceptionCode.PRICE_NOT_FOUND));
    }
}
