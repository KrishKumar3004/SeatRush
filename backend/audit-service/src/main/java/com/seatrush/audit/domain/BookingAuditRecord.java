package com.seatrush.audit.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "booking_audit", uniqueConstraints = @UniqueConstraint(columnNames = "event_id"))
public class BookingAuditRecord {
    @Id private UUID id;
    @Column(name = "event_id", nullable = false) private UUID eventId;
    @Column(name = "booking_id", nullable = false) private UUID bookingId;
    @Column(name = "show_id", nullable = false) private UUID showId;
    @Column(name = "seat_id", nullable = false) private UUID seatId;
    @Column(name = "seat_label", nullable = false) private String seatLabel;
    @Column(name = "customer_email", nullable = false) private String customerEmail;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal price;
    @Column(name = "occurred_at", nullable = false) private Instant occurredAt;
    @Column(name = "received_at", nullable = false) private Instant receivedAt;

    protected BookingAuditRecord() {}

    public BookingAuditRecord(UUID id, UUID eventId, UUID bookingId, UUID showId, UUID seatId,
                              String seatLabel, String customerEmail, BigDecimal price,
                              Instant occurredAt, Instant receivedAt) {
        this.id = id; this.eventId = eventId; this.bookingId = bookingId; this.showId = showId;
        this.seatId = seatId; this.seatLabel = seatLabel; this.customerEmail = customerEmail;
        this.price = price; this.occurredAt = occurredAt; this.receivedAt = receivedAt;
    }

    public UUID getId() { return id; }
    public UUID getEventId() { return eventId; }
    public UUID getBookingId() { return bookingId; }
    public UUID getShowId() { return showId; }
    public UUID getSeatId() { return seatId; }
    public String getSeatLabel() { return seatLabel; }
    public String getCustomerEmail() { return customerEmail; }
    public BigDecimal getPrice() { return price; }
    public Instant getOccurredAt() { return occurredAt; }
    public Instant getReceivedAt() { return receivedAt; }
}

