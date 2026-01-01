package com.chil.ticketingservice.domain.show.service;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import com.chil.ticketingservice.domain.show.dto.response.ShowCreateResponse;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // 공연 삭제 비지니스 로직 처리 메서드
    @Transactional
    public void showDelete(Long showId) {
        Show show = showRepository.findById(showId)
                        .orElseThrow(
                                () -> new CustomException(ExceptionCode.SHOW_NOT_FOUND)
                        );

        showRepository.deleteById(showId);
    }

    // 공연 조회 리스트 비지니스 로직 처리 메서드
    @Transactional(readOnly = true)
    public List<ShowResponse> showList() {
        return showRepository.findAll()
                .stream()
                .map(ShowResponse::from)
                .toList();
    }
}
