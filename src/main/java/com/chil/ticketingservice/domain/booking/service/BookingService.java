package com.chil.ticketingservice.domain.booking.service;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.booking.dto.request.BookingCreateRequest;
import com.chil.ticketingservice.domain.booking.dto.response.BookingCancelResponse;
import com.chil.ticketingservice.domain.booking.dto.response.BookingCreateResponse;
import com.chil.ticketingservice.domain.booking.dto.response.BookingDetailResponse;
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
    public BookingCreateResponse createBooking(Long userId, BookingCreateRequest request) {
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
        List<Price> prices = priceRepository.findByShow_Id(request.showId());
        boolean isPriceValid = prices.stream()
                .anyMatch(price -> price.getPrice().equals(request.price()));

        if (!isPriceValid) {
            throw new CustomException(ExceptionCode.BOOKING_PRICE_MISMATCH);
        }

        // 5. 예매 생성 - 모든 검증 통과 시 예매 생성 및 저장
        Booking booking = Booking.createBooking(user, show, request.seat(), request.price());
        Booking savedBooking = bookingRepository.save(booking);

        return BookingCreateResponse.from(savedBooking);
    }

    @Transactional
    public BookingCancelResponse cancelBooking(Long userId, Long bookingId) {
        // 1. 예매 조회
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException(ExceptionCode.BOOKING_NOT_FOUND));

        // 2. 예매 소유자 확인 - 본인의 예매만 취소 가능
        if (!booking.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.ACCESS_DENIED);
        }

        // 3. 이미 취소된 예매인지 확인
        if (booking.getIsCanceled()) {
            throw new CustomException(ExceptionCode.BOOKING_ALREADY_CANCELED);
        }

        // 4. 예매 취소 처리
        booking.cancelBooking();

        return BookingCancelResponse.from(booking);
    }

    @Transactional(readOnly = true)
    public BookingDetailResponse getBookingDetail(Long userId, Long bookingId) {
        // 1. 예매 조회
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException(ExceptionCode.BOOKING_NOT_FOUND));

        // 2. 본인 예매인지 검증
        if (!booking.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.BOOKING_ACCESS_DENIED);
        }

        // 3. DTO 변환
        return BookingDetailResponse.from(booking);
    }
}
