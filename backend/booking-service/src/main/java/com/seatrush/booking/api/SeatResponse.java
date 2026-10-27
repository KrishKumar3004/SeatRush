package com.seatrush.booking.api;

import com.seatrush.booking.domain.Seat;
import com.seatrush.booking.domain.SeatStatus;
import java.math.BigDecimal;
import java.util.UUID;

public record SeatResponse(UUID id, UUID showId, String label, String section, BigDecimal price, SeatStatus status) {
    public static SeatResponse from(Seat seat) {
        return new SeatResponse(seat.getId(), seat.getShowId(), seat.getLabel(), seat.getSection(),
                seat.getPrice(), seat.getStatus());
    }
}

