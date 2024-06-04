package com.kjh.ticketreserve;

import com.kjh.ticketreserve.model.ShowtimeSeatStatus;

public record ShowtimeSeatResponse(long id, SeatRowCode rowCode, int number, ShowtimeSeatStatus status) {
}
