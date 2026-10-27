package com.seatrush.booking.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bookings", uniqueConstraints = @UniqueConstraint(columnNames = "seat_id"))
public class Booking {
    @Id private UUID id;
    @Column(name = "show_id", nullable = false) private UUID showId;
    @Column(name = "seat_id", nullable = false) private UUID seatId;
    @Column(name = "customer_email", nullable = false) private String customerEmail;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private BookingStatus status;
    @Column(name = "created_at", nullable = false) private Instant createdAt;

    protected Booking() {}

    public Booking(UUID id, UUID showId, UUID seatId, String customerEmail, Instant createdAt) {
        this.id = id;
        this.showId = showId;
        this.seatId = seatId;
        this.customerEmail = customerEmail;
        this.status = BookingStatus.CONFIRMED;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getShowId() { return showId; }
    public UUID getSeatId() { return seatId; }
    public String getCustomerEmail() { return customerEmail; }
    public BookingStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}

