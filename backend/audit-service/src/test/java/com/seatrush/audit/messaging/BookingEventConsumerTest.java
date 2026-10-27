package com.seatrush.audit.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.seatrush.audit.repository.BookingAuditRepository;
import org.junit.jupiter.api.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingEventConsumerTest {
    @Test
    void duplicateEventIsIgnored() throws Exception {
        BookingAuditRepository repository = mock(BookingAuditRepository.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        UUID eventId = UUID.randomUUID();
        when(repository.existsByEventId(eventId)).thenReturn(true);
        BookingEventConsumer consumer = new BookingEventConsumer(repository, mapper,
                Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC));
        BookingConfirmedEvent event = new BookingConfirmedEvent(eventId, UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), "A1", "fan@example.com",
                new java.math.BigDecimal("149.00"), Instant.parse("2026-01-01T00:00:00Z"));

        consumer.consume(mapper.writeValueAsString(event));

        verify(repository, never()).save(any());
    }
}
