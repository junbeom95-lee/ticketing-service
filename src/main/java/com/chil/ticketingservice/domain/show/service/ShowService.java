package com.chil.ticketingservice.domain.show.service;

import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import com.chil.ticketingservice.domain.show.dto.response.ShowCreateResponse;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;

    // 공연 생성 비지니스 처리 로직 메서드
    @Transactional
    public ShowCreateResponse createShow(ShowCreateRequest request) {
        Show show = new Show(
                request.getTitle(),
                request.getLocation(),
                request.getShowDate(),
                request.getAgeRating(),
                request.getDescription()
        );

        showRepository.save(show);

        return ShowCreateResponse.from(show);
    }
}
