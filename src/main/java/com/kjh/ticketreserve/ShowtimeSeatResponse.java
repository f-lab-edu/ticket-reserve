package com.kjh.ticketreserve;

public record ShowtimeSeatResponse(long id, SeatRowCode rowCode, int number, ShowtimeSeatStatus status) {
}
