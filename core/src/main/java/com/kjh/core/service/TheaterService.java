package com.kjh.core.service;

import com.kjh.core.dto.TheaterSearchCondition;
import com.kjh.core.model.Theater;
import com.kjh.core.exception.BadRequestException;
import com.kjh.core.exception.NotFoundException;
import com.kjh.core.jpa.SeatRepository;
import com.kjh.core.jpa.TheaterRepository;
import com.kjh.core.model.Seat;
import com.kjh.core.model.SeatRowCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    public TheaterService(TheaterRepository theaterRepository, SeatRepository seatRepository) {
        this.theaterRepository = theaterRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public Theater createTheater(String name, String address) {
        Theater theater = new Theater();
        theater.setName(name);
        theater.setAddress(address);
        return theaterRepository.save(theater);
    }

    @Transactional(readOnly = true)
    public Theater getTheater(long id) {
        return theaterRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_THEATER);
    }

    @Transactional
    public Theater updateTheater(long id, String name, String address) {
        Theater theater = theaterRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_THEATER);
        theater.setName(name);
        theater.setAddress(address);
        return theater;
    }

    @Transactional
    public void deleteTheater(long id) {
        Theater theater = theaterRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_THEATER);
        theaterRepository.delete(theater);
    }

    @Transactional(readOnly = true)
    public Page<Theater> searchTheaters(int pageNumber, int pageSize, String name, String address) {
        TheaterSearchCondition searchCondition = new TheaterSearchCondition(name, address);
        return theaterRepository.findAllBySearchCondition(searchCondition, PageRequest.of(pageNumber, pageSize));
    }

    @Transactional
    public Seat createSeat(long theaterId, SeatRowCode rowCode, int number) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(NotFoundException.NOT_FOUND_THEATER);

        boolean exists = seatRepository.existsByTheaterIdAndRowCodeAndNumber(theater.getId(), rowCode, number);
        if (exists) {
            throw BadRequestException.DUPLICATED_SEAT.get();
        }
        Seat seat = new Seat();
        seat.setTheater(theater);
        seat.setRowCode(rowCode);
        seat.setNumber(number);
        return seatRepository.save(seat);
    }

    @Transactional(readOnly = true)
    public Seat getSeat(long id) {
        return seatRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SEAT);
    }

    @Transactional
    public Seat updateSeat(long id, SeatRowCode rowCode, int number) {
        Seat seat = seatRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SEAT);
        seat.setRowCode(rowCode);
        seat.setNumber(number);
        return seat;
    }

    @Transactional
    public void deleteSeat(long id) {
        Seat seat = seatRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SEAT);
        seatRepository.delete(seat);
    }

    @Transactional(readOnly = true)
    public List<Seat> getSeats(long theaterId) {
        return seatRepository.findByTheaterIdOrderByRowCodeAscNumberAsc(theaterId);
    }
}
