package com.seatrush.audit.repository;

import com.seatrush.audit.domain.BookingAuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BookingAuditRepository extends JpaRepository<BookingAuditRecord, UUID> {
    boolean existsByEventId(UUID eventId);
    List<BookingAuditRecord> findTop100ByOrderByReceivedAtDesc();
}

