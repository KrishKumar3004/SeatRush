package com.seatrush.booking.application;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BookingConfirmedEvent(
        UUID eventId,
        UUID bookingId,
        UUID showId,
        UUID seatId,
        String seatLabel,
        String customerEmail,
        BigDecimal price,
        Instant occurredAt) {}

