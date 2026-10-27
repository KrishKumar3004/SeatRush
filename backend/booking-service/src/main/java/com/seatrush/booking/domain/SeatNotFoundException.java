package com.seatrush.booking.domain;

import java.util.UUID;

public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(UUID seatId) { super("Seat " + seatId + " was not found"); }
}

