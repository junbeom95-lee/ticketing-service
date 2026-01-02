package com.chil.ticketingservice.domain.show.repository;

import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShowCustomRepository {

    Page<ShowResponse> ShowSearch(Pageable pageable);
}
