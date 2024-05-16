package com.kjh.ticketreserve.service;

import com.kjh.ticketreserve.SeatRequest;
import com.kjh.ticketreserve.TheaterRequest;
import com.kjh.ticketreserve.TheaterSearchCondition;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.SeatRepository;
import com.kjh.ticketreserve.jpa.TheaterRepository;
import com.kjh.ticketreserve.model.Seat;
import com.kjh.ticketreserve.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    public TheaterService(TheaterRepository theaterRepository, SeatRepository seatRepository) {
        this.theaterRepository = theaterRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public void createTheater(Theater theater) {
        theaterRepository.save(theater);
    }

    @Transactional(readOnly = true)
    public Theater getTheater(long id) {
        return theaterRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND::get);
    }

    @Transactional
    public Theater updateTheater(long id, TheaterRequest theaterRequest) {
        Theater theater = theaterRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND::get);
        theater.setName(theaterRequest.name());
        theater.setAddress(theaterRequest.address());
        return theater;
    }

    @Transactional
    public void deleteTheater(long id) {
        Theater theater = theaterRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND::get);
        theaterRepository.delete(theater);
    }

    @Transactional(readOnly = true)
    public Page<Theater> searchTheaters(int pageNumber, int pageSize, String name, String address) {
        TheaterSearchCondition searchCondition = new TheaterSearchCondition(name, address);
        return theaterRepository.findAllBySearchCondition(searchCondition, PageRequest.of(pageNumber, pageSize));
    }

    @Transactional
    public void createSeat(Seat seat) {
        boolean exists = seatRepository.existsByTheaterIdAndRowCodeAndNumber(seat.getTheater().getId(),
            seat.getRowCode(),
            seat.getNumber());
        if (exists) {
            throw BadRequestException.DUPLICATED_SEAT.get();
        }
        seatRepository.save(seat);
    }

    @Transactional(readOnly = true)
    public Seat getSeat(long id) {
        return seatRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_SEAT::get);
    }

    @Transactional
    public Seat updateSeat(long id, SeatRequest seatRequest) {
        Seat seat = seatRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND::get);
        seat.setRowCode(seatRequest.rowCode());
        seat.setNumber(seatRequest.number());
        return seat;
    }
}
