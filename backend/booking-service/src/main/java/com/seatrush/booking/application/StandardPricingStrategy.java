package com.seatrush.booking.application;

import com.seatrush.booking.domain.Seat;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal priceFor(Seat seat) {
        return seat.getPrice();
    }
}

