package com.kjh.ticketreserve.service;

import com.kjh.ticketreserve.ShowtimeUpdateRequest;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.ShowtimeRepository;
import com.kjh.ticketreserve.model.Showtime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    @Transactional
    public void createShowtime(Showtime showtime) {
        boolean exists = showtimeRepository.existsByMovieIdAndTheaterIdAndShowtime(showtime.getMovie().getId(),
            showtime.getTheater().getId(),
            showtime.getShowtime());
        if (exists) {
            throw BadRequestException.DUPLICATED_SHOWTIME.get();
        }
        showtimeRepository.save(showtime);
    }

    @Transactional(readOnly = true)
    public Showtime getShowtime(long id) {
        return showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME::get);
    }

    @Transactional
    public Showtime updateShowtime(long id, ShowtimeUpdateRequest showtimeUpdateRequest) {
        Showtime showtime = showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME::get);
        showtime.setShowtime(showtimeUpdateRequest.showtime());
        return showtime;
    }

    @Transactional
    public void deleteShowtime(long id) {
        Showtime showtime = showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME::get);
        showtimeRepository.delete(showtime);
    }
}
