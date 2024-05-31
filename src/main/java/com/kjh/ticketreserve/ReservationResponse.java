package com.kjh.ticketreserve;

import com.kjh.ticketreserve.model.Reservation;

public record ReservationResponse(
    long id,
    ShowtimeResponse showtime,
    SeatResponse seat,
    int price,
    ReservationStatus status
) {

    public ReservationResponse(Reservation reservation, ShowtimeResponse showtime, SeatResponse seat) {
        this(reservation.getId(), showtime, seat, reservation.getPrice(), reservation.getStatus());
    }
}
