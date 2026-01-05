package com.chil.ticketingservice.domain.search.repository;

import com.chil.ticketingservice.domain.search.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long> {
}
