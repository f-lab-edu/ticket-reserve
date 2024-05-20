package com.kjh.ticketreserve.service;

import com.kjh.ticketreserve.ShowtimeSearchCondition;
import com.kjh.ticketreserve.ShowtimeSeat;
import com.kjh.ticketreserve.ShowtimeUpdateRequest;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.SeatRepository;
import com.kjh.ticketreserve.jpa.ShowtimeRepository;
import com.kjh.ticketreserve.model.Showtime;
import com.kjh.ticketreserve.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final TheaterService theaterService;

    public ShowtimeService(ShowtimeRepository showtimeRepository, SeatRepository seatRepository, TheaterService theaterService) {
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
        this.theaterService = theaterService;
    }

    @Transactional
    public void createShowtime(Showtime showtime) {
        boolean exists = showtimeRepository.existsByMovieIdAndTheaterIdAndShowDatetime(showtime.getMovie().getId(),
            showtime.getTheater().getId(),
            showtime.getShowDatetime());
        if (exists) {
            throw BadRequestException.DUPLICATED_SHOWTIME.get();
        }
        showtimeRepository.save(showtime);
    }

    @Transactional(readOnly = true)
    public Showtime getShowtime(long id) {
        return showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME);
    }

    @Transactional
    public Showtime updateShowtime(long id, ShowtimeUpdateRequest showtimeUpdateRequest) {
        Showtime showtime = showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME);
        showtime.setShowDatetime(showtimeUpdateRequest.showDatetime());
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
        Theater theater = theaterService.getTheater(showtime.getTheater().getId());
        return seatRepository.findShowtimeSeats(theater.getId(), showtime.getId());
    }
}
