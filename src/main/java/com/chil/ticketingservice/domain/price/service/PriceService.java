package com.chil.ticketingservice.domain.price.service;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.price.dto.request.ShowSeatPriceRegRequest;
import com.chil.ticketingservice.domain.price.dto.response.PriceShowSeatOneResponse;
import com.chil.ticketingservice.domain.price.dto.response.PriceShowSeatResponse;
import com.chil.ticketingservice.domain.price.dto.response.ShowSeatPriceRegResponse;
import com.chil.ticketingservice.domain.price.entity.Price;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.price.repository.PriceRepository;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final ShowRepository showRepository;
    private final PriceRepository priceRepository;

    // 공연별 좌석 금액 등록 비지니스 로직 처리 메서드
    @Transactional
    public ShowSeatPriceRegResponse showSeatPriceReg(
            Long showId,
            ShowSeatPriceRegRequest request
    ) {
        Show show = showRepository.findShowById(showId);

        SeatTypeEnum seatType = request.seatType();

        boolean existsSeatPrice = priceRepository.existsByShow_IdAndSeatType(showId, seatType);

        if (existsSeatPrice) {
            throw new CustomException(ExceptionCode.SHOW_SEAT_DUPLICATED);
        }

        Price price = new Price(
                show,
                seatType,
                request.price()
        );

        Price priceSeatSave = priceRepository.save(price);

        return ShowSeatPriceRegResponse.from(priceSeatSave);
    }

    //공연별 좌석 금액 목록 조회
    @Transactional(readOnly = true)
    public List<PriceShowSeatResponse> getShowSeatPriceList(Long showId) {

        Show show = showRepository.findShowById(showId);

        List<Price> priceList = priceRepository.findByShow(show);

        return priceList
                .stream()
                .map(p -> new PriceShowSeatResponse(p.getSeatType(), p.getPrice()))
                .toList();
    }

    //공연별 좌석 금액 단건 조회
    @Transactional(readOnly = true)
    public PriceShowSeatOneResponse getPriceShowSeatOne(Long showId, SeatTypeEnum seatType) {

        Show show = showRepository.findShowById(showId);

        Price price = priceRepository.findWithShowAndSeatType(show, seatType);

        return new PriceShowSeatOneResponse(price.getPrice());
    }
}
