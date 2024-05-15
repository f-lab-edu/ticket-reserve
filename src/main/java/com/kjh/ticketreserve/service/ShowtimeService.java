package com.kjh.ticketreserve.service;

import com.kjh.ticketreserve.ShowtimeSearchCondition;
import com.kjh.ticketreserve.ShowtimeUpdateRequest;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.ShowtimeRepository;
import com.kjh.ticketreserve.model.Showtime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
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
        return showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME::get);
    }

    @Transactional
    public Showtime updateShowtime(long id, ShowtimeUpdateRequest showtimeUpdateRequest) {
        Showtime showtime = showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME::get);
        showtime.setShowDatetime(showtimeUpdateRequest.showDatetime());
        return showtime;
    }

    @Transactional
    public void deleteShowtime(long id) {
        Showtime showtime = showtimeRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME::get);
        showtimeRepository.delete(showtime);
    }

    @Transactional(readOnly = true)
    public Page<Showtime> searchShowtimes(int pageNumber, int pageSize, Long movieId, Long theaterId, LocalDate date) {
        ShowtimeSearchCondition searchCondition = new ShowtimeSearchCondition(movieId, theaterId, date);
        return showtimeRepository.findAllBySearchCondition(searchCondition, PageRequest.of(pageNumber, pageSize));
    }
}
