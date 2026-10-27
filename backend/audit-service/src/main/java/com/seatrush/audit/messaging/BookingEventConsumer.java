package com.seatrush.audit.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatrush.audit.domain.BookingAuditRecord;
import com.seatrush.audit.repository.BookingAuditRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.util.UUID;

@Component
public class BookingEventConsumer {
    private final BookingAuditRepository audits;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public BookingEventConsumer(BookingAuditRepository audits, ObjectMapper objectMapper, Clock clock) {
        this.audits = audits;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    @KafkaListener(topics = "booking.confirmed.v1", groupId = "audit-service-v1")
    @Transactional
    public void consume(String payload) throws JsonProcessingException {
        BookingConfirmedEvent event = objectMapper.readValue(payload, BookingConfirmedEvent.class);
        if (audits.existsByEventId(event.eventId())) return;

        audits.save(new BookingAuditRecord(UUID.randomUUID(), event.eventId(), event.bookingId(),
                event.showId(), event.seatId(), event.seatLabel(), event.customerEmail(), event.price(),
                event.occurredAt(), clock.instant()));
    }
}

