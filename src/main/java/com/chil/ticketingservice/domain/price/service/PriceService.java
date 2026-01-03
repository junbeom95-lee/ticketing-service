package com.chil.ticketingservice.domain.price.service;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.price.dto.request.ShowSeatPriceRegRequest;
import com.chil.ticketingservice.domain.price.dto.response.ShowSeatPriceRegResponse;
import com.chil.ticketingservice.domain.price.entity.Price;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.price.repository.PriceRepository;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
