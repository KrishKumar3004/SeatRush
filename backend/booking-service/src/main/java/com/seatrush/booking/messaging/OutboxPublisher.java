package com.seatrush.booking.messaging;

import com.seatrush.booking.domain.OutboxEvent;
import com.seatrush.booking.repository.OutboxEventRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;

@Component
@ConditionalOnProperty(name = "seatrush.outbox.enabled", havingValue = "true", matchIfMissing = true)
public class OutboxPublisher {
    private final OutboxEventRepository outbox;
    private final KafkaTemplate<String, String> kafka;
    private final Clock clock;

    public OutboxPublisher(OutboxEventRepository outbox, KafkaTemplate<String, String> kafka, Clock clock) {
        this.outbox = outbox;
        this.kafka = kafka;
        this.clock = clock;
    }

    @Scheduled(fixedDelayString = "${seatrush.outbox.interval-ms:500}")
    @Transactional
    public void publish() {
        for (OutboxEvent event : outbox.findTop50ByPublishedAtIsNullOrderByCreatedAt()) {
            kafka.send("booking.confirmed.v1", event.getAggregateId().toString(), event.getPayload()).join();
            event.markPublished(clock.instant());
        }
    }
}

