package com.kjh.ticketreserve.service;

import com.kjh.ticketreserve.ReservationStatus;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.ReservationRepository;
import com.kjh.ticketreserve.jpa.SeatRepository;
import com.kjh.ticketreserve.model.Reservation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    public ReservationService(ReservationRepository reservationRepository, SeatRepository seatRepository) {
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public void createReservation(Reservation reservation) {
        seatRepository.findByIdWithLock(reservation.getSeat().getId())
            .orElseThrow(NotFoundException.NOT_FOUND_SEAT);

        boolean isReserved = reservationRepository.existsByShowtimeIdAndSeatIdAndStatus(
            reservation.getShowtime().getId(),
            reservation.getSeat().getId(),
            ReservationStatus.RESERVED);
        if (isReserved) {
            throw BadRequestException.ALREADY_RESERVED_SEAT.get();
        }
        reservationRepository.save(reservation);
    }
}
