package com.kjh.ticketreserve;

import com.kjh.ticketreserve.model.Seat;
import com.kjh.ticketreserve.model.ShowtimeSeat;
import com.kjh.ticketreserve.model.ShowtimeSeatStatus;

public record ShowtimeSeatResponse(long id, SeatRowCode rowCode, int number, ShowtimeSeatStatus status) {

    public ShowtimeSeatResponse(ShowtimeSeat showtimeSeat, Seat seat) {
        this(showtimeSeat.getId(), seat.getRowCode(), seat.getNumber(), showtimeSeat.getStatus());
    }
}
