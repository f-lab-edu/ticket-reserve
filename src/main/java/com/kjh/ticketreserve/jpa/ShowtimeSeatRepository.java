package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.model.ShowtimeSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, Long> {

    List<ShowtimeSeat> findByShowtimeId(Long showtimeId);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<ShowtimeSeat> findByShowtimeIdAndSeatId(Long showtimeId, Long seatId);
}
