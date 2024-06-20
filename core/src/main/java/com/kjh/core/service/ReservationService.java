package com.kjh.core.service;

import com.kjh.core.exception.BadRequestException;
import com.kjh.core.exception.NotFoundException;
import com.kjh.core.jpa.ReservationRepository;
import com.kjh.core.jpa.ShowtimeSeatRepository;
import com.kjh.core.model.*;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final UserService userService;
    private final ShowtimeService showtimeService;
    private final TheaterService theaterService;
    private final MovieService movieService;
    private final SendExternalService sendExternalService;
    private final ReservationRepository reservationRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;

    public ReservationService(UserService userService,
                              ShowtimeService showtimeService,
                              TheaterService theaterService,
                              MovieService movieService,
                              SendExternalService sendExternalService,
                              ReservationRepository reservationRepository,
                              ShowtimeSeatRepository showtimeSeatRepository) {
        this.userService = userService;
        this.showtimeService = showtimeService;
        this.theaterService = theaterService;
        this.movieService = movieService;
        this.sendExternalService = sendExternalService;
        this.reservationRepository = reservationRepository;
        this.showtimeSeatRepository = showtimeSeatRepository;
    }

    @Transactional
    public Reservation createReservation(long userId, long showtimeId, long seatId) {
        User user = userService.getUser(userId);
        Showtime showtime = showtimeService.getShowtime(showtimeId);
        Seat seat = theaterService.getSeat(seatId);
        Movie movie = movieService.getMovie(showtime.getMovie().getId());

        ShowtimeSeat showtimeSeat = showtimeSeatRepository.findByShowtimeIdAndSeatId(showtime.getId(), seat.getId())
            .orElseThrow(NotFoundException.NOT_FOUND_SHOWTIME_SEAT);

        if (ShowtimeSeatStatus.RESERVED.equals(showtimeSeat.getStatus())) {
            throw BadRequestException.ALREADY_RESERVED_SEAT.get();
        }

        showtimeSeat.setStatus(ShowtimeSeatStatus.RESERVED);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setShowtime(showtime);
        reservation.setSeat(seat);
        reservation.setPrice(movie.getPrice());
        reservation.setStatus(ReservationStatus.RESERVED);

        try {
            reservationRepository.saveAndFlush(reservation);
        } catch (OptimisticLockingFailureException e) {
            throw BadRequestException.ALREADY_RESERVED_SEAT.get();
        }

        sendExternalService.sendReservationConfirmed(reservation, showtime, seat, showtime.getTheater(), user);

        return reservation;
    }
}
