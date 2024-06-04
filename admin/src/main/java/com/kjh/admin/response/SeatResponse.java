package com.kjh.admin.response;

import com.kjh.core.model.Seat;
import com.kjh.core.model.SeatRowCode;

public record SeatResponse(long id, SeatRowCode rowCode, int number) {

    public SeatResponse(Seat seat) {
        this(seat.getId(), seat.getRowCode(), seat.getNumber());
    }
}
