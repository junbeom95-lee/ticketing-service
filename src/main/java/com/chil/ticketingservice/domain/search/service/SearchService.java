package com.chil.ticketingservice.domain.search.service;

import com.chil.ticketingservice.domain.search.dto.request.SearchCreateRequest;
import com.chil.ticketingservice.domain.search.entity.Search;
import com.chil.ticketingservice.domain.search.repository.SearchRepository;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final ShowRepository showRepository;

    @Transactional
    public Page<ShowResponse> totalSearch(SearchCreateRequest request, Long userId, Pageable pageable) {
        // 아무것도 검색 안 하면 백지라도 출력.
        if (request == null || request.searchData().isBlank()) {
            return Page.empty(pageable);
        }

        Search search = new Search(request.searchData(), userId);

        searchRepository.save(search);

        return showRepository.totalSearch(request, pageable);
    }
}
