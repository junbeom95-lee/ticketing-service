package com.chil.ticketingservice.domain.booking.service;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.booking.dto.request.BookingCreateRequest;
import com.chil.ticketingservice.domain.booking.dto.response.BookingCancelResponse;
import com.chil.ticketingservice.domain.booking.dto.response.BookingCreateResponse;
import com.chil.ticketingservice.domain.booking.dto.response.BookingDetailResponse;
import com.chil.ticketingservice.domain.booking.dto.response.BookingListResponse;
import com.chil.ticketingservice.domain.booking.dto.response.BookingPaymentResponse;
import com.chil.ticketingservice.domain.booking.entity.Booking;
import com.chil.ticketingservice.domain.booking.repository.BookingRepository;
import com.chil.ticketingservice.domain.price.entity.Price;
import com.chil.ticketingservice.domain.price.repository.PriceRepository;
import com.chil.ticketingservice.domain.seat.entity.Seat;
import com.chil.ticketingservice.domain.seat.repository.SeatRepository;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import com.chil.ticketingservice.domain.user.entity.User;
import com.chil.ticketingservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final PriceRepository priceRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingCreateResponse createBooking(Long userId, BookingCreateRequest request) {
        // 1. 사용자 조회 - userId로 사용자 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        // 2. 공연 존재 확인 - showId로 공연 조회
        Show show = showRepository.findById(request.showId())
                .orElseThrow(() -> new CustomException(ExceptionCode.SHOW_NOT_FOUND));

        // 3. 해당 공연 일시를 확인 - 공연이 지나면 예매 불가능
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(show.getShowDate())) {
            throw new CustomException(ExceptionCode.BOOKING_CANNOT_AFTER_SHOW);
        }

        // 4. 좌석 중복 확인 - 해당 공연의 좌석이 예매 가능인지 확인
        Seat seat = seatRepository.findSeatBySeatCode(show, request.seat());
        if (!seat.getSeatStatus()) {
            throw new CustomException(ExceptionCode.SEAT_ALREADY_BOOKED);
        }

        // 5. 가격 검증 - 요청한 가격이 해당 공연의 유효한 가격인지 확인
        List<Price> priceList = priceRepository.findByShow(show);
        boolean isPriceValid = priceList.stream()
                .anyMatch(price -> price.getPrice().equals(request.price()));

        if (!isPriceValid) {
            throw new CustomException(ExceptionCode.BOOKING_PRICE_MISMATCH);
        }

        // 6. 예매 생성 - 모든 검증 통과 시 예매 생성 및 저장
        Booking booking = Booking.createBooking(user, show, request.seat(), request.price());
        Booking savedBooking = bookingRepository.save(booking);
        seat.bookSeat();

        return BookingCreateResponse.from(savedBooking);
    }

    @Transactional
    public BookingCancelResponse cancelBooking(Long userId, Long bookingId) {
        // 1. 예매 조회
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException(ExceptionCode.BOOKING_NOT_FOUND));

        // 2. 끝난 공연인지 확인
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(booking.getShow().getShowDate())) {
            throw new CustomException(ExceptionCode.BOOKING_CANNOT_CANCEL_AFTER_SHOW);
        }

        // 3. 예매 소유자 확인 - 본인의 예매만 취소 가능
        if (!Objects.equals(booking.getUser().getId(), userId)) {
            throw new CustomException(ExceptionCode.BOOKING_ACCESS_DENIED);
        }

        // 4. 이미 취소된 예매인지 확인
        if (booking.getIsCanceled()) {
            throw new CustomException(ExceptionCode.BOOKING_ALREADY_CANCELED);
        }

        // 5. 예매 취소 처리
        booking.cancelBooking();

        // 6. 예매 가능한 좌석으로 변경
        Seat seat = seatRepository.findSeatBySeatCode(booking.getShow(), booking.getSeat());
        seat.availableSeat();

        return BookingCancelResponse.from(booking);
    }

    @Transactional(readOnly = true)
    public BookingDetailResponse getBookingDetail(Long userId, Long bookingId) {
        // 1. 예매 조회
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException(ExceptionCode.BOOKING_NOT_FOUND));

        // 2. 본인 예매인지 검증
        if (!Objects.equals(booking.getUser().getId(), userId)) {
            throw new CustomException(ExceptionCode.BOOKING_ACCESS_DENIED);
        }

        // 3. DTO 변환
        return BookingDetailResponse.from(booking);
    }

    @Transactional
    public BookingPaymentResponse processPayment(Long userId, Long bookingId) {
        // 1. 예매 조회
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException(ExceptionCode.BOOKING_NOT_FOUND));

        // 2. 예매 소유자 확인 - 본인의 예매만 결제 가능
        if (!Objects.equals(booking.getUser().getId(), userId)) {
            throw new CustomException(ExceptionCode.BOOKING_ACCESS_DENIED);
        }

        // 3. 이미 결제된 예매인지 확인
        if (booking.getPaymentStatus()) {
            throw new CustomException(ExceptionCode.BOOKING_ALREADY_PAID);
        }

        // 4. 취소된 예매인지 확인
        if (booking.getIsCanceled()) {
            throw new CustomException(ExceptionCode.BOOKING_CANCELED_CANNOT_PAY);
        }

        // 5. 결제 처리
        booking.processPayment();

        return BookingPaymentResponse.from(booking);
    }

    @Transactional(readOnly = true)
    public Page<BookingListResponse> getUserBookings(Long authenticatedUserId, Long userId, Pageable pageable) {
        // 1. 본인 확인 - 인증된 사용자만 본인의 예매 조회 가능
        if (!Objects.equals(authenticatedUserId, userId)) {
            throw new CustomException(ExceptionCode.BOOKING_ACCESS_DENIED);
        }

        // 2. 해당 사용자의 모든 예매 조회 (페이징)
        Page<Booking> bookings = bookingRepository.findAllByUser_Id(userId, pageable);

        // 3. DTO 페이지로 변환 (빈 페이지도 200 OK로 정상 응답)
        return bookings.map(BookingListResponse::from);
    }
}
