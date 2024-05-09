package com.kjh.ticketreserve.service;

import com.kjh.ticketreserve.TheaterRequest;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.TheaterRepository;
import com.kjh.ticketreserve.model.Theater;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
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
}
