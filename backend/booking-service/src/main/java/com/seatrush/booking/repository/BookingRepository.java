package com.seatrush.booking.repository;

import com.seatrush.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {}

