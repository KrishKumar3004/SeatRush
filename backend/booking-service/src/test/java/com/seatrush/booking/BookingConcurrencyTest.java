package com.seatrush.booking;

import com.seatrush.booking.application.BookingApplicationService;
import com.seatrush.booking.domain.Seat;
import com.seatrush.booking.domain.SeatUnavailableException;
import com.seatrush.booking.repository.BookingRepository;
import com.seatrush.booking.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookingConcurrencyTest {
    @Autowired BookingApplicationService bookingService;
    @Autowired SeatRepository seats;
    @Autowired BookingRepository bookings;
    private UUID seatId;

    @BeforeEach
    void setUp() {
        bookings.deleteAll();
        seats.deleteAll();
        seatId = UUID.randomUUID();
        seats.saveAndFlush(new Seat(seatId, UUID.randomUUID(), "A1", "VIP", new BigDecimal("149.00")));
    }

    @Test
    void exactlyOneOfTwentyConcurrentRequestsBooksTheSeat() throws Exception {
        int contenders = 20;
        ExecutorService pool = Executors.newFixedThreadPool(contenders);
        CountDownLatch ready = new CountDownLatch(contenders);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger wins = new AtomicInteger();

        try {
            Future<?>[] attempts = new Future<?>[contenders];
            for (int i = 0; i < contenders; i++) {
                int customer = i;
                attempts[i] = pool.submit(() -> {
                    ready.countDown();
                    start.await();
                    try {
                        bookingService.reserve(seatId, "fan" + customer + "@example.com");
                        wins.incrementAndGet();
                    } catch (SeatUnavailableException ignored) {
                        // Expected for every contender after the transaction holding the row lock commits.
                    }
                    return null;
                });
            }
            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();
            for (Future<?> attempt : attempts) attempt.get(10, TimeUnit.SECONDS);
        } finally {
            pool.shutdownNow();
        }

        assertThat(wins).hasValue(1);
        assertThat(bookings.count()).isEqualTo(1);
    }
}
