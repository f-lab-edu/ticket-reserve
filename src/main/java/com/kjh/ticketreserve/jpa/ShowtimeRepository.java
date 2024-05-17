package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long>, ShowtimeCustomRepository {

    boolean existsByMovieIdAndTheaterIdAndShowDatetime(Long movieId, Long theaterId, LocalDateTime showDatetime);
}
