package com.chil.ticketingservice.domain.price.repository;

import com.chil.ticketingservice.domain.price.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {

    List<Price> findByShowId(Long showId);
}
