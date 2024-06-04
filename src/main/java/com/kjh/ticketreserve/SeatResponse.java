package com.kjh.ticketreserve;

import com.kjh.ticketreserve.model.Seat;

public record SeatResponse(long id, SeatRowCode rowCode, int number) {

    public SeatResponse(Seat seat) {
        this(seat.getId(), seat.getRowCode(), seat.getNumber());
    }
}
