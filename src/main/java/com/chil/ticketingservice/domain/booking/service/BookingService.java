package com.chil.ticketingservice.domain.booking.service;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.booking.dto.request.BookingCreateRequest;
import com.chil.ticketingservice.domain.booking.dto.response.BookingResponse;
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
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final PriceRepository priceRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponse createBooking(Long userId, BookingCreateRequest request) {
        // 1. 사용자 조회 - userId로 사용자 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        // 2. 공연 존재 확인 - showId로 공연 조회
        Show show = showRepository.findById(request.showId())
                .orElseThrow(() -> new CustomException(ExceptionCode.SHOW_NOT_FOUND));

        // 3. 좌석 중복 확인 - 해당 공연의 동일 좌석 중 취소되지 않은 예매 여부 검증
        bookingRepository.findByShowIdAndSeatAndIsCanceledFalse(request.showId(), request.seat())
                .ifPresent(booking -> {
                    throw new CustomException(ExceptionCode.SEAT_ALREADY_BOOKED);
                });

        // 4. 가격 검증 - 요청한 가격이 해당 공연의 유효한 가격인지 확인
        List<Price> prices = priceRepository.findByShowId(request.showId());
        boolean isPriceValid = prices.stream()
                .anyMatch(price -> price.getSeatPrice() == request.price());

        if (!isPriceValid) {
            throw new CustomException(ExceptionCode.BOOKING_PRICE_MISMATCH);
        }

        // 5. 예매 생성 - 모든 검증 통과 시 예매 생성 및 저장
        Booking booking = Booking.createBooking(user, show, request.seat(), request.price());
        Booking savedBooking = bookingRepository.save(booking);

        return BookingResponse.from(savedBooking);
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {
        // 1. 예매 조회
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException(ExceptionCode.BOOKING_NOT_FOUND));

        // 2. 이미 취소된 예매인지 확인
        if (booking.getIsCanceled()) {
            throw new CustomException(ExceptionCode.BOOKING_ALREADY_CANCELED);
        }

        // 3. 예매 취소 처리
        booking.cancelBooking();

        return BookingResponse.from(booking);
    }


}
