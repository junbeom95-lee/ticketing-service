package com.chil.ticketingservice.domain.show.service;

import com.chil.ticketingservice.domain.like.repository.LikeRepository;
import com.chil.ticketingservice.domain.seat.service.SeatService;
import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import com.chil.ticketingservice.domain.show.dto.request.ShowSearchRequest;
import com.chil.ticketingservice.domain.show.dto.response.ShowCreateResponse;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final SeatService seatService;
    private final LikeRepository likeRepository;

    // 공연 생성 비지니스 처리 로직 메서드
    @Transactional
    public ShowCreateResponse createShow(ShowCreateRequest request) {
        Show show = new Show(request);

        Show showSave = showRepository.save(show);

        seatService.createSeat(showSave);

        return ShowCreateResponse.from(showSave);
    }

    // 공연 삭제 비지니스 로직 처리 메서드
    @Transactional
    public void showDelete(Long showId) {
        showRepository.findShowById(showId);

        showRepository.deleteById(showId);
    }

    // 공연 조회 리스트 비지니스 로직 처리 메서드
    @Transactional(readOnly = true)
    public Page<ShowResponse> showList(ShowSearchRequest request, Pageable pageable) {
        return showRepository.showSearch(request, pageable);
    }

    // 공연 상세 조회 비지니스 로직 처리 메서드
    @Transactional(readOnly = true)
    public ShowResponse showDetail(Long showId) {
        Show show = showRepository.findShowById(showId);

        long likeCnt = likeRepository.countByShow(show);

        return ShowResponse.from(show, likeCnt);
    }
}
