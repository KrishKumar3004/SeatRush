package com.seatrush.booking.config;

import com.seatrush.booking.domain.Seat;
import com.seatrush.booking.repository.SeatRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "seatrush.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DemoDataInitializer implements ApplicationRunner {
    private final SeatRepository seats;
    private final UUID showId;

    public DemoDataInitializer(SeatRepository seats,
            @Value("${seatrush.demo-show-id}") UUID showId) {
        this.seats = seats;
        this.showId = showId;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!seats.findAllByShowIdOrderByLabel(showId).isEmpty()) return;
        List<Seat> demoSeats = new ArrayList<>();
        for (char row = 'A'; row <= 'E'; row++) {
            for (int number = 1; number <= 8; number++) {
                String label = row + String.valueOf(number);
                String section = row <= 'B' ? "VIP" : "STANDARD";
                BigDecimal price = row <= 'B' ? new BigDecimal("149.00") : new BigDecimal("79.00");
                UUID id = UUID.nameUUIDFromBytes((showId + ":" + label).getBytes(StandardCharsets.UTF_8));
                demoSeats.add(new Seat(id, showId, label, section, price));
            }
        }
        seats.saveAll(demoSeats);
    }
}

