package com.seatrush.booking.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatrush.booking.domain.*;
import com.seatrush.booking.repository.BookingRepository;
import com.seatrush.booking.repository.OutboxEventRepository;
import com.seatrush.booking.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class BookingApplicationService {
    private final SeatRepository seats;
    private final BookingRepository bookings;
    private final OutboxEventRepository outbox;
    private final PricingStrategy pricing;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public BookingApplicationService(SeatRepository seats, BookingRepository bookings,
                                     OutboxEventRepository outbox, PricingStrategy pricing,
                                     ObjectMapper objectMapper, Clock clock) {
        this.seats = seats;
        this.bookings = bookings;
        this.outbox = outbox;
        this.pricing = pricing;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    @Transactional
    public BookingResult reserve(UUID seatId, String customerEmail) {
        Seat seat = seats.findByIdForUpdate(seatId).orElseThrow(() -> new SeatNotFoundException(seatId));
        seat.book();

        Instant now = clock.instant();
        Booking booking = bookings.save(new Booking(
                UUID.randomUUID(), seat.getShowId(), seat.getId(), customerEmail, now));
        BigDecimal price = pricing.priceFor(seat);
        UUID eventId = UUID.randomUUID();
        BookingConfirmedEvent event = new BookingConfirmedEvent(
                eventId, booking.getId(), seat.getShowId(), seat.getId(), seat.getLabel(),
                customerEmail, price, now);
        outbox.save(new OutboxEvent(eventId, booking.getId(), "BookingConfirmed", json(event), now));

        return new BookingResult(booking.getId(), seat.getShowId(), seat.getId(), seat.getLabel(),
                booking.getStatus(), price, now);
    }

    private String json(BookingConfirmedEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize booking event", exception);
        }
    }
}

