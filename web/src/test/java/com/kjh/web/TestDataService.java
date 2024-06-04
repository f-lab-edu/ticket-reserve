package com.kjh.web;

import com.kjh.core.jpa.*;
import com.kjh.core.model.Movie;
import com.kjh.core.model.Seat;
import com.kjh.core.model.Showtime;
import com.kjh.core.model.Theater;
import com.kjh.core.service.MovieService;
import com.kjh.core.service.ShowtimeService;
import com.kjh.core.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TestDataService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    TheaterRepository theaterRepository;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    ShowtimeRepository showtimeRepository;

    @Autowired
    ShowtimeSeatRepository showtimeSeatRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    MovieService movieService;

    @Autowired
    TheaterService theaterService;

    @Autowired
    ShowtimeService showtimeService;

    public void deleteAll() {
        reservationRepository.deleteAll();
        showtimeSeatRepository.deleteAll();
        showtimeRepository.deleteAll();
        seatRepository.deleteAll();
        theaterRepository.deleteAll();
        movieRepository.deleteAll();
    }

    public Movie createMovie(MovieRequest movieRequest) {
        return movieService.createMovie(movieRequest.title(),
            movieRequest.startDate(),
            movieRequest.endDate(),
            movieRequest.price());
    }

    public Theater createTheater(TheaterRequest theaterRequest) {
        return theaterService.createTheater(theaterRequest.name(), theaterRequest.address());
    }

    public Seat createSeat(long theaterId, SeatRequest seatRequest) {
        return theaterService.createSeat(theaterId, seatRequest.rowCode(), seatRequest.number());
    }

    public Showtime createShowtime(long movieId, long theaterId, LocalDateTime showDatetime) {
        return showtimeService.createShowtime(movieId, theaterId, showDatetime.truncatedTo(ChronoUnit.SECONDS));
    }
}
