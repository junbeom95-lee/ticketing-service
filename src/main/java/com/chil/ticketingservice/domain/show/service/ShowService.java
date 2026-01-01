package com.chil.ticketingservice.domain.show.service;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import com.chil.ticketingservice.domain.show.dto.request.ShowSeatPriceRegRequest;
import com.chil.ticketingservice.domain.show.dto.response.ShowCreateResponse;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.chil.ticketingservice.domain.show.dto.response.ShowSeatPriceRegResponse;
import com.chil.ticketingservice.domain.show.entity.SeatPrice;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.show.repository.SeatPriceRepository;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final SeatPriceRepository seatPriceRepository;

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

        Show showSave = showRepository.save(show);

        return ShowCreateResponse.from(showSave);
    }

    // 공연 삭제 비지니스 로직 처리 메서드
    @Transactional
    public void showDelete(Long showId) {
        Show show = showRepository.findShowById(showId);

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

    // 공연 상세 조회 비지니스 로직 처리 메서드
    @Transactional(readOnly = true)
    public ShowResponse showDetail(Long showId) {
        Show show = showRepository.findShowById(showId);

        return ShowResponse.from(show);
    }

    // 공연별 좌석 금액 등록 비지니스 로직 처리 메서드
    @Transactional
    public ShowSeatPriceRegResponse showSeatPriceReg(
            Long showId,
            ShowSeatPriceRegRequest request
    ) {
        Show show = showRepository.findShowById(showId);

        SeatTypeEnum seatType = SeatTypeEnum.of(request.getSeatType());

        boolean existsSeatPrice = seatPriceRepository.existsByShow_IdAndSeatType(showId, seatType);

        if (existsSeatPrice) {
            throw new CustomException(ExceptionCode.SEAT_ALREADY_EXISTS);
        }

        SeatPrice seatPrice = new SeatPrice(
                show,
                seatType,
                request.getPrice()
        );

        SeatPrice seatPriceSave = seatPriceRepository.save(seatPrice);

        return ShowSeatPriceRegResponse.from(seatPriceSave);
    }
}
