package com.chil.ticketingservice.domain.booking.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.chil.ticketingservice.domain.booking.dto.request.BookingCreateRequest;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrencyTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;

    int numUsers = 3;

    @BeforeEach
    void setup() {

        List<User> users = IntStream.rangeClosed(1, numUsers)
            .mapToObj(i ->
                new User("test" + i + "@test.com", "test" + i,
                    LocalDate.now(), "1234", UserRole.USER)
            )
            .toList();
        userRepository.saveAll(users);

        ShowCreateRequest request = new ShowCreateRequest("test title", "DDP", LocalDateTime.now().plusDays(3), 15L, "descriptions", "image");
        Show show = new Show(request);
        showRepository.save(show);

        Seat seat = new Seat(show, SeatTypeEnum.A, 1);
        seatRepository.save(seat);

        Price price = new Price(show, SeatTypeEnum.A, 10000);
        priceRepository.save(price);
    }

    @Test
    @DisplayName("동일 좌석을 동시에 예매 시도")
    void createBooking_concurrency_test() throws InterruptedException {

        // Given
        BookingCreateRequest request = new BookingCreateRequest(1L, "A1", 10000);

        ExecutorService executorService = Executors.newFixedThreadPool(numUsers);
        CountDownLatch ready = new CountDownLatch(numUsers);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(numUsers);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();

        // When
        for (int i = 1; i <= numUsers; i++) {

            long userId = i;

            executorService.execute(() -> {
                ready.countDown();
                try {
                    start.await();
                    bookingService.createBooking(userId, request);
                    success.incrementAndGet();
                } catch (Exception e) {
                    failure.incrementAndGet();
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        start.countDown();
        done.await();

        executorService.shutdown();

        // Then
        assertThat(success.get()).isEqualTo(1);
        assertThat(failure.get()).isEqualTo(2);

        Seat seat = seatRepository.findById(1L).orElseThrow();
        assertThat(seat.getSeatStatus()).isFalse();
        assertThat(bookingRepository.count()).isEqualTo(1);
    }
}