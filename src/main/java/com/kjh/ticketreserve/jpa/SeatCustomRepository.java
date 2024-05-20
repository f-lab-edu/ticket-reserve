package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.ShowtimeSeat;

import java.util.List;

public interface SeatCustomRepository {

    List<ShowtimeSeat> findShowtimeSeats(Long theaterId, Long showtimeId);
}
