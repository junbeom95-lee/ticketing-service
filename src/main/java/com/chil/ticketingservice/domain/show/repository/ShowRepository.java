package com.chil.ticketingservice.domain.show.repository;

import com.chil.ticketingservice.domain.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {
}
