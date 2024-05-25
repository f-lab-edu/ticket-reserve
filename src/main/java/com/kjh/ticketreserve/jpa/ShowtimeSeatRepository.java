package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.model.ShowtimeSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, Long> {

    List<ShowtimeSeat> findByShowtimeId(Long showtimeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ShowtimeSeat s where s.showtime.id = :showtimeId and s.seat.id = :seatId")
    Optional<ShowtimeSeat> findByShowtimeIdAndSeatIdForUpdate(@Param("showtimeId") Long showtimeId,
                                                              @Param("seatId") Long seatId);
}
