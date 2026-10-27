package com.seatrush.booking.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "seats", uniqueConstraints = @UniqueConstraint(columnNames = {"show_id", "label"}))
public class Seat {
    @Id private UUID id;
    @Column(name = "show_id", nullable = false) private UUID showId;
    @Column(nullable = false) private String label;
    @Column(nullable = false) private String section;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal price;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SeatStatus status;
    @Version private long version;

    protected Seat() {}

    public Seat(UUID id, UUID showId, String label, String section, BigDecimal price) {
        this.id = id;
        this.showId = showId;
        this.label = label;
        this.section = section;
        this.price = price;
        this.status = SeatStatus.AVAILABLE;
    }

    public void book() {
        if (status != SeatStatus.AVAILABLE) throw new SeatUnavailableException(id);
        status = SeatStatus.BOOKED;
    }

    public UUID getId() { return id; }
    public UUID getShowId() { return showId; }
    public String getLabel() { return label; }
    public String getSection() { return section; }
    public BigDecimal getPrice() { return price; }
    public SeatStatus getStatus() { return status; }
}

