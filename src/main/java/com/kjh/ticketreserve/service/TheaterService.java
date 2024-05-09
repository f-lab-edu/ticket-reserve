package com.kjh.ticketreserve.service;

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
}
