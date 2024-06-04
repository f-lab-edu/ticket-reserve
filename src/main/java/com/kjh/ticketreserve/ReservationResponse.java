package com.kjh.ticketreserve;

public record ReservationResponse(
    long id,
    ShowtimeResponse showtime,
    SeatResponse seat,
    int price,
    ReservationStatus status
) {
}
