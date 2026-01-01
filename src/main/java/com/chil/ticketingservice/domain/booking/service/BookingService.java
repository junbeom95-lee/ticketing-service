package com.chil.ticketingservice.domain.booking.service;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.booking.dto.req.BookingCreateRequest;
import com.chil.ticketingservice.domain.booking.dto.res.BookingResponse;
import com.chil.ticketingservice.domain.booking.entity.Booking;
import com.chil.ticketingservice.domain.booking.repository.BookingRepository;
import com.chil.ticketingservice.domain.price.entity.Price;
import com.chil.ticketingservice.domain.price.repository.PriceRepository;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import com.chil.ticketingservice.domain.user.entity.User;
import com.chil.ticketingservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final PriceRepository priceRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponse createBooking(Long userId, BookingCreateRequest request) {
        // 1. 사용자 조회 - userId로 사용자 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.EXISTS_EMAIL)); // TODO: USER_NOT_FOUND 예외 추가 필요

        // 2. 공연 존재 확인 - showId로 공연 조회
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new CustomException(ExceptionCode.SHOW_NOT_FOUND));

        // 3. 좌석 중복 확인 - 해당 공연의 동일 좌석 예매 여부 검증
        bookingRepository.findByShowIdAndSeat(request.getShowId(), request.getSeat())
                .ifPresent(booking -> {
                    throw new CustomException(ExceptionCode.SEAT_ALREADY_BOOKED);
                });

        // 4. 가격 검증 - 요청한 가격이 해당 공연의 유효한 가격인지 확인
        List<Price> prices = priceRepository.findByShowId(request.getShowId());
        boolean isPriceValid = prices.stream()
                .anyMatch(price -> price.getSeatPrice() == request.getPrice());

        if (!isPriceValid) {
            throw new CustomException(ExceptionCode.INVALID_PRICE);
        }

        // 5. 예매 생성 - 모든 검증 통과 시 예매 생성 및 저장
        Booking booking = Booking.createBooking(user, show, request.getSeat(), request.getPrice());
        Booking savedBooking = bookingRepository.save(booking);

        return BookingResponse.from(savedBooking);
    }
}
