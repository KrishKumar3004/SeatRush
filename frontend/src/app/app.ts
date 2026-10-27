import { CurrencyPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatToolbarModule } from '@angular/material/toolbar';

type SeatStatus = 'AVAILABLE' | 'BOOKED';

interface Seat {
  id: string;
  showId: string;
  label: string;
  section: string;
  price: number;
  status: SeatStatus;
}

interface BookingResult {
  bookingId: string;
  seatLabel: string;
  status: 'CONFIRMED';
}

@Component({
  selector: 'app-root',
  imports: [CurrencyPipe, FormsModule, MatButtonModule, MatCardModule, MatChipsModule,
    MatFormFieldModule, MatIconModule, MatInputModule, MatProgressSpinnerModule, MatToolbarModule],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  private readonly http = inject(HttpClient);
  private readonly showId = '11111111-1111-1111-1111-111111111111';
  readonly seats = signal<Seat[]>([]);
  readonly selected = signal<Seat | null>(null);
  readonly loading = signal(true);
  readonly booking = signal(false);
  readonly message = signal('');
  readonly availableCount = computed(() => this.seats().filter((seat) => seat.status === 'AVAILABLE').length);
  email = '';

  ngOnInit(): void { this.loadSeats(); }

  select(seat: Seat): void {
    if (seat.status !== 'AVAILABLE' || this.booking()) return;
    this.selected.set(seat);
    this.message.set('');
  }

  reserve(): void {
    const seat = this.selected();
    if (!seat || !this.email) return;
    this.booking.set(true);
    this.message.set('');
    this.http.post<BookingResult>(`/api/seats/${seat.id}/bookings`, { customerEmail: this.email }).subscribe({
      next: (result) => {
        this.message.set(`Seat ${result.seatLabel} is yours. Booking ${result.bookingId.slice(0, 8)} confirmed.`);
        this.selected.set(null);
        this.loadSeats();
      },
      error: (error) => {
        this.booking.set(false);
        this.message.set(error.status === 409
          ? 'Another fan just booked that seat. Pick another available seat.'
          : 'Booking failed. Please try again.');
        this.loadSeats();
      },
    });
  }

  private loadSeats(): void {
    this.loading.set(true);
    this.http.get<Seat[]>(`/api/shows/${this.showId}/seats`).subscribe({
      next: (seats) => {
        this.seats.set(seats);
        this.loading.set(false);
        this.booking.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.booking.set(false);
        this.message.set('The booking service is unavailable. Start the backend and refresh.');
      },
    });
  }
}

