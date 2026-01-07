package com.chil.ticketingservice.domain.booking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.seat.repository.SeatRepository;
import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import com.chil.ticketingservice.domain.user.entity.User;
import com.chil.ticketingservice.domain.user.enums.UserRole;
import com.chil.ticketingservice.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PriceRepository priceRepository;

    User user;
    User otherUser;
    Show show;
    Show pastShow;
    Seat seat;
    Price price;

    @BeforeEach
    void setUp() {
        user = new User("test@test.com", "testUser", LocalDate.now(), "1234", UserRole.USER);
        userRepository.save(user);

        otherUser = new User("other@test.com", "otherUser", LocalDate.now(), "1234", UserRole.USER);
        userRepository.save(otherUser);

        ShowCreateRequest showRequest = new ShowCreateRequest(
                "Test Show",
                "Test Location",
                LocalDateTime.now().plusDays(7),
                15L,
                "Test Description",
                "test-image.jpg"
        );
        show = new Show(showRequest);
        showRepository.save(show);

        ShowCreateRequest pastShowRequest = new ShowCreateRequest(
                "Past Show",
                "Test Location",
                LocalDateTime.now().minusDays(1),
                15L,
                "Past Show Description",
                "past-show.jpg"
        );
        pastShow = new Show(pastShowRequest);
        showRepository.save(pastShow);

        seat = new Seat(show, SeatTypeEnum.A, 1);
        seatRepository.save(seat);

        price = new Price(show, SeatTypeEnum.A, 10000);
        priceRepository.save(price);
    }

    @Test
    @DisplayName("예매 생성 성공")
    void createBooking_success() {
        // Given
        BookingCreateRequest request = new BookingCreateRequest(show.getId(), "A1", 10000);

        // When
        BookingCreateResponse response = bookingService.createBooking(user.getId(), request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.seat()).isEqualTo("A1");
        assertThat(response.paymentStatus()).isFalse();

        Seat bookedSeat = seatRepository.findById(seat.getId()).orElseThrow();
        assertThat(bookedSeat.getSeatStatus()).isFalse();
    }

    @Test
    @DisplayName("예매 생성 실패 - 사용자를 찾을 수 없음")
    void createBooking_userNotFound() {
        // Given
        BookingCreateRequest request = new BookingCreateRequest(show.getId(), "A1", 10000);
        Long invalidUserId = 999999L;

        // When & Then
        assertThatThrownBy(() -> bookingService.createBooking(invalidUserId, request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("예매 생성 실패 - 공연을 찾을 수 없음")
    void createBooking_showNotFound() {
        // Given
        BookingCreateRequest request = new BookingCreateRequest(999L, "A1", 10000);

        // When & Then
        assertThatThrownBy(() -> bookingService.createBooking(user.getId(), request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.SHOW_NOT_FOUND);
    }

    @Test
    @DisplayName("예매 생성 실패 - 공연이 이미 지남")
    void createBooking_showAlreadyPassed() {
        // Given
        Seat pastSeat = new Seat(pastShow, SeatTypeEnum.A, 1);
        seatRepository.save(pastSeat);

        Price pastPrice = new Price(pastShow, SeatTypeEnum.A, 10000);
        priceRepository.save(pastPrice);

        BookingCreateRequest request = new BookingCreateRequest(pastShow.getId(), "A1", 10000);

        // When & Then
        assertThatThrownBy(() -> bookingService.createBooking(user.getId(), request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_CANNOT_AFTER_SHOW);
    }

    @Test
    @DisplayName("예매 생성 실패 - 좌석이 이미 예매됨")
    void createBooking_seatAlreadyBooked() {
        // Given
        seat.bookSeat();
        seatRepository.save(seat);

        BookingCreateRequest request = new BookingCreateRequest(show.getId(), "A1", 10000);

        // When & Then
        assertThatThrownBy(() -> bookingService.createBooking(user.getId(), request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.SEAT_ALREADY_BOOKED);
    }

    @Test
    @DisplayName("예매 생성 실패 - 가격이 일치하지 않음")
    void createBooking_priceMismatch() {
        // Given
        BookingCreateRequest request = new BookingCreateRequest(show.getId(), "A1", 99999);

        // When & Then
        assertThatThrownBy(() -> bookingService.createBooking(user.getId(), request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_PRICE_MISMATCH);
    }

    @Test
    @DisplayName("예매 취소 성공")
    void cancelBooking_success() {
        // Given
        Booking booking = Booking.createBooking(user, show.getId(), "A1", 10000);
        bookingRepository.save(booking);
        seat.bookSeat();

        // When
        BookingCancelResponse response = bookingService.cancelBooking(user.getId(), booking.getBookingId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isCanceled()).isTrue();

        Booking canceledBooking = bookingRepository.findById(booking.getBookingId()).orElseThrow();
        assertThat(canceledBooking.getIsCanceled()).isTrue();
        assertThat(canceledBooking.getPaymentStatus()).isFalse();

        Seat availableSeat = seatRepository.findById(seat.getId()).orElseThrow();
        assertThat(availableSeat.getSeatStatus()).isTrue();
    }

    @Test
    @DisplayName("예매 취소 실패 - 예매를 찾을 수 없음")
    void cancelBooking_bookingNotFound() {
        // Given
        Long invalidBookingId = 999999L;

        // When & Then
        assertThatThrownBy(() -> bookingService.cancelBooking(user.getId(), invalidBookingId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_NOT_FOUND);
    }

    @Test
    @DisplayName("예매 취소 실패 - 공연이 이미 끝남")
    void cancelBooking_showAlreadyEnded() {
        // Given
        Seat pastSeat = new Seat(pastShow, SeatTypeEnum.A, 1);
        seatRepository.save(pastSeat);

        Booking booking = Booking.createBooking(user, pastShow.getId(), "A1", 10000);
        bookingRepository.save(booking);

        // When & Then
        assertThatThrownBy(() -> bookingService.cancelBooking(user.getId(), booking.getBookingId()))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_CANNOT_CANCEL_AFTER_SHOW);
    }

    @Test
    @DisplayName("예매 취소 실패 - 본인의 예매가 아님")
    void cancelBooking_accessDenied() {
        // Given
        Booking booking = Booking.createBooking(user, show.getId(), "A1", 10000);
        bookingRepository.save(booking);

        // When & Then
        assertThatThrownBy(() -> bookingService.cancelBooking(otherUser.getId(), booking.getBookingId()))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_ACCESS_DENIED);
    }

    @Test
    @DisplayName("예매 취소 실패 - 이미 취소된 예매")
    void cancelBooking_alreadyCanceled() {
        // Given
        Booking booking = Booking.createBooking(user, show.getId(), "A1", 10000);
        booking.cancelBooking();
        bookingRepository.save(booking);

        // When & Then
        assertThatThrownBy(() -> bookingService.cancelBooking(user.getId(), booking.getBookingId()))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_ALREADY_CANCELED);
    }

    @Test
    @DisplayName("예매 상세 조회 성공")
    void getBookingDetail_success() {
        // Given
        Booking booking = Booking.createBooking(user, show.getId(), "A1", 10000);
        bookingRepository.save(booking);

        // When
        BookingDetailResponse response = bookingService.getBookingDetail(user.getId(), booking.getBookingId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.seat()).isEqualTo("A1");
        assertThat(response.paymentStatus()).isFalse();
        assertThat(response.isCanceled()).isFalse();
    }

    @Test
    @DisplayName("예매 상세 조회 실패 - 예매를 찾을 수 없음")
    void getBookingDetail_bookingNotFound() {
        // Given
        Long invalidBookingId = 999999L;

        // When & Then
        assertThatThrownBy(() -> bookingService.getBookingDetail(user.getId(), invalidBookingId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_NOT_FOUND);
    }

    @Test
    @DisplayName("예매 상세 조회 실패 - 본인의 예매가 아님")
    void getBookingDetail_accessDenied() {
        // Given
        Booking booking = Booking.createBooking(user, show.getId(), "A1", 10000);
        bookingRepository.save(booking);

        // When & Then
        assertThatThrownBy(() -> bookingService.getBookingDetail(otherUser.getId(), booking.getBookingId()))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_ACCESS_DENIED);
    }

    @Test
    @DisplayName("결제 처리 성공")
    void processPayment_success() {
        // Given
        Booking booking = Booking.createBooking(user, show.getId(), "A1", 10000);
        bookingRepository.save(booking);

        // When
        BookingPaymentResponse response = bookingService.processPayment(user.getId(), booking.getBookingId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.paymentStatus()).isTrue();

        Booking paidBooking = bookingRepository.findById(booking.getBookingId()).orElseThrow();
        assertThat(paidBooking.getPaymentStatus()).isTrue();
    }

    @Test
    @DisplayName("결제 처리 실패 - 예매를 찾을 수 없음")
    void processPayment_bookingNotFound() {
        // Given
        Long invalidBookingId = 999999L;

        // When & Then
        assertThatThrownBy(() -> bookingService.processPayment(user.getId(), invalidBookingId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_NOT_FOUND);
    }

    @Test
    @DisplayName("결제 처리 실패 - 본인의 예매가 아님")
    void processPayment_accessDenied() {
        // Given
        Booking booking = Booking.createBooking(user, show.getId(), "A1", 10000);
        bookingRepository.save(booking);

        // When & Then
        assertThatThrownBy(() -> bookingService.processPayment(otherUser.getId(), booking.getBookingId()))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_ACCESS_DENIED);
    }

    @Test
    @DisplayName("결제 처리 실패 - 이미 결제됨")
    void processPayment_alreadyPaid() {
        // Given
        Booking booking = Booking.createBooking(user, show.getId(), "A1", 10000);
        booking.processPayment();
        bookingRepository.save(booking);

        // When & Then
        assertThatThrownBy(() -> bookingService.processPayment(user.getId(), booking.getBookingId()))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_ALREADY_PAID);
    }

    @Test
    @DisplayName("결제 처리 실패 - 취소된 예매")
    void processPayment_canceledBooking() {
        // Given
        Booking booking = Booking.createBooking(user, show.getId(), "A1", 10000);
        booking.cancelBooking();
        bookingRepository.save(booking);

        // When & Then
        assertThatThrownBy(() -> bookingService.processPayment(user.getId(), booking.getBookingId()))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_CANCELED_CANNOT_PAY);
    }

    @Test
    @DisplayName("사용자 예매 목록 조회 성공")
    void getUserBookings_success() {
        // Given
        Booking booking1 = Booking.createBooking(user, show.getId(), "A1", 10000);
        Booking booking2 = Booking.createBooking(user, show.getId(), "A2", 10000);
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        PageRequest pageRequest = PageRequest.of(0, 10);

        // When
        Page<BookingListResponse> response = bookingService.getUserBookings(user.getId(), user.getId(), pageRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTotalElements()).isEqualTo(2);
        assertThat(response.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("사용자 예매 목록 조회 성공 - 빈 목록")
    void getUserBookings_emptyList() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When
        Page<BookingListResponse> response = bookingService.getUserBookings(user.getId(), user.getId(), pageRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTotalElements()).isEqualTo(0);
        assertThat(response.getContent()).isEmpty();
    }

    @Test
    @DisplayName("사용자 예매 목록 조회 실패 - 본인이 아님")
    void getUserBookings_accessDenied() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When & Then
        assertThatThrownBy(() -> bookingService.getUserBookings(user.getId(), otherUser.getId(), pageRequest))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.BOOKING_ACCESS_DENIED);
    }
}
