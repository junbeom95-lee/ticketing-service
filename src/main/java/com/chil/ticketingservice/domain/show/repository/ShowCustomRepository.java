package com.chil.ticketingservice.domain.show.repository;

import com.chil.ticketingservice.domain.show.dto.request.ShowSearchRequest;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShowCustomRepository {

    Page<ShowResponse> showSearch(ShowSearchRequest request, Pageable pageable);
}
