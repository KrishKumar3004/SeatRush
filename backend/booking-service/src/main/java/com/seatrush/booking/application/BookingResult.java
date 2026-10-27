package com.seatrush.booking.application;

import com.seatrush.booking.domain.BookingStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BookingResult(
        UUID bookingId,
        UUID showId,
        UUID seatId,
        String seatLabel,
        BookingStatus status,
        BigDecimal price,
        Instant createdAt) {}

