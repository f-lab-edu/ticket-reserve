package com.kjh.admin.response;

import com.kjh.core.model.Reservation;
import com.kjh.core.model.ReservationStatus;

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
