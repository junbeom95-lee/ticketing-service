package com.chil.ticketingservice.domain.show.service;

import com.chil.ticketingservice.domain.booking.service.BookingService;
import com.chil.ticketingservice.domain.like.repository.LikeRepository;
import com.chil.ticketingservice.domain.price.repository.PriceRepository;
import com.chil.ticketingservice.domain.seat.repository.SeatRepository;
import com.chil.ticketingservice.domain.seat.service.SeatService;
import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import com.chil.ticketingservice.domain.show.dto.request.ShowSearchRequest;
import com.chil.ticketingservice.domain.show.dto.response.SearchRankResponse;
import com.chil.ticketingservice.domain.show.dto.response.ShowCreateResponse;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final PriceRepository priceRepository;
    private final LikeRepository likeRepository;
    private final ShowSearchRedisService showSearchRedisService;
    private final BookingService bookingService;
    private final S3Service s3Service;

    // 공연 생성 비지니스 처리 로직 메서드
    @Transactional
    public ShowCreateResponse createShow(ShowCreateRequest request, MultipartFile image) {

        String imageUrl = s3Service.upload(image);

        ShowCreateRequest newRequest = new ShowCreateRequest(
                request.title(),
                request.location(),
                request.showDate(),
                request.ageRating(),
                request.description(),
                imageUrl
        );

        Show show = new Show(newRequest);

        Show showSave = showRepository.save(show);

        seatService.createSeat(showSave);

        return ShowCreateResponse.from(showSave);
    }

    // 공연 삭제 비지니스 로직 처리 메서드
    @Transactional
    public void showDelete(Long showId) {

        Show show = showRepository.findShowById(showId);

        priceRepository.deleteByShowId(showId);

        seatRepository.deleteByShowId(showId);

        bookingService.deleteShowCancelBooking(show);

        showRepository.deleteById(showId);
    }

    // 공연 조회 리스트 비지니스 로직 처리 메서드
    @Transactional(readOnly = true)
    public Page<ShowResponse> showList(Long userId, ShowSearchRequest request, Pageable pageable) {
        Page<ShowResponse> result = showRepository.showSearch(request, pageable);

        if (request.showTitle() != null && !request.showTitle().isBlank()) {
            showSearchRedisService.searchLogSave(
                    userId,
                    request.showTitle()
            );
        }

        return result;
    }

    // 공연 상세 조회 비지니스 로직 처리 메서드
    @Transactional(readOnly = true)
    public ShowResponse showDetail(Long showId) {
        Show show = showRepository.findShowById(showId);

        long likeCnt = likeRepository.countByShow(show);

        return ShowResponse.from(show, likeCnt);
    }

    // 인기 검색어 순위 비지니스 로직 처리 메서드
    @Transactional(readOnly = true)
    public List<SearchRankResponse> searchRankList(int limit) {
        List<SearchRankResponse> result = showSearchRedisService.searchRankList(limit);

        return result;
    }
}
