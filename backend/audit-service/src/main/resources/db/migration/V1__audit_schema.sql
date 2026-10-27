CREATE TABLE booking_audit (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL UNIQUE,
    booking_id UUID NOT NULL,
    show_id UUID NOT NULL,
    seat_id UUID NOT NULL,
    seat_label VARCHAR(20) NOT NULL,
    customer_email VARCHAR(320) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    received_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_booking_audit_received ON booking_audit (received_at DESC);

