package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.SeatRowCode;
import com.kjh.ticketreserve.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    boolean existsByTheaterIdAndRowCodeAndNumber(Long theaterId, SeatRowCode rowCode, int number);
}
