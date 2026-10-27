package com.seatrush.audit.api;

import com.seatrush.audit.domain.BookingAuditRecord;
import com.seatrush.audit.repository.BookingAuditRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit/bookings")
public class AuditController {
    private final BookingAuditRepository audits;

    public AuditController(BookingAuditRepository audits) { this.audits = audits; }

    @GetMapping
    public List<BookingAuditRecord> recent() {
        return audits.findTop100ByOrderByReceivedAtDesc();
    }
}

