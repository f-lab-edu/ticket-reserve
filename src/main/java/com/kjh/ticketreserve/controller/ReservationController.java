package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.*;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.UserRepository;
import com.kjh.ticketreserve.model.*;
import com.kjh.ticketreserve.service.MovieService;
import com.kjh.ticketreserve.service.ReservationService;
import com.kjh.ticketreserve.service.ShowtimeService;
import com.kjh.ticketreserve.service.TheaterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final ShowtimeService showtimeService;
    private final TheaterService theaterService;
    private final MovieService movieService;
    private final UserRepository userRepository;

    public ReservationController(ReservationService reservationService, ShowtimeService showtimeService, TheaterService theaterService, MovieService movieService, UserRepository userRepository) {
        this.reservationService = reservationService;
        this.showtimeService = showtimeService;
        this.theaterService = theaterService;
        this.movieService = movieService;
        this.userRepository = userRepository;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest reservationRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByEmail(username).orElseThrow(NotFoundException.NOT_FOUND_USER);
        Showtime showtime = showtimeService.getShowtime(reservationRequest.showtimeId());
        Seat seat = theaterService.getSeat(reservationRequest.seatId());
        Movie movie = movieService.getMovie(showtime.getMovie().getId());

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setShowtime(showtime);
        reservation.setSeat(seat);
        reservation.setPrice(movie.getPrice());
        reservation.setStatus(ReservationStatus.RESERVED);
        reservationService.createReservation(reservation);

        return ResponseEntity.status(201).body(new ReservationResponse(reservation.getId(),
            new ShowtimeResponse(showtime.getId(), new MovieResponse(movie), new TheaterResponse(showtime.getTheater()), showtime.getShowDatetime()),
            new SeatResponse(seat),
            reservation.getPrice(),
            reservation.getStatus()));
    }
}
