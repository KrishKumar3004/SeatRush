package com.seatrush.booking.domain;

import java.util.UUID;

public class SeatUnavailableException extends RuntimeException {
    public SeatUnavailableException(UUID seatId) {
        super("Seat " + seatId + " is no longer available");
    }
}

