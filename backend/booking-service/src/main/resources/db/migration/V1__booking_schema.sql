CREATE TABLE seats (
    id UUID PRIMARY KEY,
    show_id UUID NOT NULL,
    label VARCHAR(20) NOT NULL,
    section VARCHAR(40) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_seat_show_label UNIQUE (show_id, label)
);

CREATE TABLE bookings (
    id UUID PRIMARY KEY,
    show_id UUID NOT NULL,
    seat_id UUID NOT NULL,
    customer_email VARCHAR(320) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uk_booking_seat UNIQUE (seat_id),
    CONSTRAINT fk_booking_seat FOREIGN KEY (seat_id) REFERENCES seats(id)
);

CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    aggregate_id UUID NOT NULL,
    type VARCHAR(80) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_outbox_unpublished ON outbox_events (created_at) WHERE published_at IS NULL;

