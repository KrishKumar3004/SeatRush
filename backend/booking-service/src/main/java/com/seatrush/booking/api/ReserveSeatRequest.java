package com.seatrush.booking.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ReserveSeatRequest(@NotBlank @Email String customerEmail) {}

