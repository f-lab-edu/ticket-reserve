package com.kjh.ticketreserve.service;

import com.kjh.ticketreserve.ShowtimeSearchCondition;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.SeatRepository;
import com.kjh.ticketreserve.jpa.ShowtimeRepository;
import com.kjh.ticketreserve.jpa.ShowtimeSeatRepository;
import com.kjh.ticketreserve.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowtimeService {

    private final MovieService movieService;
    private final TheaterService theaterService;
    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final SeatRepository seatRepository;

    public ShowtimeService(MovieService movieService,
                           TheaterService theaterService,
                           ShowtimeRepository showtimeRepository,
                           ShowtimeSeatRepository showtimeSeatRepository,
                           SeatRepository seatRepository) {
        this.movieService = movieService;
        this.theaterService = theaterService;
        this.showtimeRepository = showtimeRepository;
        this.showtimeSeatRepository = showtimeSeatRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public Showtime createShowtime(long movieId, long theaterId, LocalDateTime showDatetime) {
        Movie movie = movieService.getMovie(movieId);
        Theater theater = theaterService.getTheater(theaterId);

        boolean exists = showtimeRepository.existsByMovieIdAndTheaterIdAndShowDatetime(movie.getId(),
            theater.getId(),
            showDatetime);
        if (exists) {
            throw BadRequestException.DUPLICATED_SHOWTIME.get();
        }
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater(theater);
        showtime.setShowDatetime(showDatetime);
        showtimeRepository.save(showtime);

        List<Seat> seats = seatRepository.findByTheaterId(showtime.getTheater().getId());
        for (Seat seat : seats) {
            ShowtimeSeat showtimeSeat = new ShowtimeSeat();
            showtimeSeat.setShowtime(showtime);
            showtimeSeat.setSeat(seat);
            showtimeSeat.setStatus(ShowtimeSeatStatus.AVAILABLE);
            showtimeSeatRepository.save(showtimeSeat);
        }
        return showtime;
    }

    @Transactional(readOnly = true)
    public Showtime getShowtime(long id) {
        return showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME);
    }

    @Transactional
    public Showtime updateShowtime(long id, LocalDateTime showDatetime) {
        Showtime showtime = showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME);
        showtime.setShowDatetime(showDatetime);
        return showtime;
    }

    @Transactional
    public void deleteShowtime(long id) {
        Showtime showtime = showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME);
        showtimeRepository.delete(showtime);
    }

    @Transactional(readOnly = true)
    public Page<Showtime> searchShowtimes(int pageNumber, int pageSize, Long movieId, Long theaterId, LocalDate date) {
        ShowtimeSearchCondition searchCondition = new ShowtimeSearchCondition(movieId, theaterId, date);
        return showtimeRepository.findAllBySearchCondition(searchCondition, PageRequest.of(pageNumber, pageSize));
    }

    public List<ShowtimeSeat> getSeats(long id) {
        Showtime showtime = showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME);
        return showtimeSeatRepository.findByShowtimeId(showtime.getId());
    }
}
