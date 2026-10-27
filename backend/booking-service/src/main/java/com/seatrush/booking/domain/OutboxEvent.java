package com.seatrush.booking.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id private UUID id;
    @Column(name = "aggregate_id", nullable = false) private UUID aggregateId;
    @Column(nullable = false) private String type;
    @Column(nullable = false, columnDefinition = "TEXT") private String payload;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "published_at") private Instant publishedAt;

    protected OutboxEvent() {}

    public OutboxEvent(UUID id, UUID aggregateId, String type, String payload, Instant createdAt) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.type = type;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public void markPublished(Instant when) { this.publishedAt = when; }
    public UUID getId() { return id; }
    public UUID getAggregateId() { return aggregateId; }
    public String getPayload() { return payload; }
    public Instant getCreatedAt() { return createdAt; }
}

