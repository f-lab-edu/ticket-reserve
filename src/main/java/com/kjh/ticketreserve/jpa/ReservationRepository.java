package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.ReservationStatus;
import com.kjh.ticketreserve.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByShowtimeIdAndSeatIdAndStatus(Long showtimeId, Long seatId, ReservationStatus status);
}
