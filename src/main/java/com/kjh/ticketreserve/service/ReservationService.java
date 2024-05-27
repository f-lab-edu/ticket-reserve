package com.kjh.ticketreserve.service;

import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.ReservationRepository;
import com.kjh.ticketreserve.jpa.ShowtimeSeatRepository;
import com.kjh.ticketreserve.model.Reservation;
import com.kjh.ticketreserve.model.ShowtimeSeat;
import com.kjh.ticketreserve.model.ShowtimeSeatStatus;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ShowtimeSeatRepository showtimeSeatRepository) {
        this.reservationRepository = reservationRepository;
        this.showtimeSeatRepository = showtimeSeatRepository;
    }

    @Transactional
    public void createReservation(Reservation reservation) {
        ShowtimeSeat showtimeSeat = showtimeSeatRepository.findByShowtimeIdAndSeatId(
            reservation.getShowtime().getId(), reservation.getSeat().getId())
            .orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME_SEAT);

        if (ShowtimeSeatStatus.RESERVED.equals(showtimeSeat.getStatus())) {
            throw BadRequestException.ALREADY_RESERVED_SEAT.get();
        }

        showtimeSeat.setStatus(ShowtimeSeatStatus.RESERVED);

        try {
            reservationRepository.saveAndFlush(reservation);
        } catch (OptimisticLockingFailureException e) {
            throw BadRequestException.ALREADY_RESERVED_SEAT.get();
        }
    }
}
