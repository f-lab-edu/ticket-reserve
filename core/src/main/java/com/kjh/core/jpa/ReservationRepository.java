package com.kjh.core.jpa;

import com.kjh.core.model.Reservation;
import com.kjh.core.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByShowtimeIdAndSeatIdAndStatus(Long showtimeId, Long seatId, ReservationStatus status);
}
