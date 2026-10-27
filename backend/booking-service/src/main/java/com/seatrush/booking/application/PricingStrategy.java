package com.seatrush.booking.application;

import com.seatrush.booking.domain.Seat;
import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal priceFor(Seat seat);
}

