package com.kjh.ticketreserve;

public record ShowtimeSeat(long id, SeatRowCode rowCode, int number, ShowtimeSeatStatus status) {

    public ShowtimeSeat(long id, SeatRowCode rowCode, int number, ReservationStatus status) {
        this(id,
            rowCode,
            number,
            ReservationStatus.RESERVED.equals(status) ? ShowtimeSeatStatus.RESERVED : ShowtimeSeatStatus.AVAILABLE);
    }
}
