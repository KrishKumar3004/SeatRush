package com.seatrush.booking.api;

import com.seatrush.booking.application.BookingApplicationService;
import com.seatrush.booking.application.BookingResult;
import com.seatrush.booking.repository.SeatRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BookingController {
    private final SeatRepository seats;
    private final BookingApplicationService bookingService;

    public BookingController(SeatRepository seats, BookingApplicationService bookingService) {
        this.seats = seats;
        this.bookingService = bookingService;
    }

    @GetMapping("/shows/{showId}/seats")
    public List<SeatResponse> seats(@PathVariable UUID showId) {
        return seats.findAllByShowIdOrderByLabel(showId).stream().map(SeatResponse::from).toList();
    }

    @PostMapping("/seats/{seatId}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResult reserve(@PathVariable UUID seatId, @Valid @RequestBody ReserveSeatRequest request) {
        return bookingService.reserve(seatId, request.customerEmail());
    }
}

