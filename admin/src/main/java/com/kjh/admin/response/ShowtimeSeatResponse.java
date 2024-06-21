package com.kjh.admin.response;

import com.kjh.core.model.Seat;
import com.kjh.core.model.SeatRowCode;
import com.kjh.core.model.ShowtimeSeat;
import com.kjh.core.model.ShowtimeSeatStatus;

public record ShowtimeSeatResponse(long id, SeatRowCode rowCode, int number, ShowtimeSeatStatus status) {

    public ShowtimeSeatResponse(ShowtimeSeat showtimeSeat, Seat seat) {
        this(seat.getId(), seat.getRowCode(), seat.getNumber(), showtimeSeat.getStatus());
    }
}
