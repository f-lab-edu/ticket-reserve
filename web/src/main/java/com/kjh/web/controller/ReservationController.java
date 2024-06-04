package com.kjh.web.controller;

import com.kjh.core.model.*;
import com.kjh.core.service.ReservationService;
import com.kjh.core.service.UserService;
import com.kjh.web.request.ReservationRequest;
import com.kjh.web.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest reservationRequest,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.getByEmail(username);
        Reservation reservation = reservationService.createReservation(user.getId(),
            reservationRequest.showtimeId(),
            reservationRequest.seatId());

        Showtime showtime = reservation.getShowtime();
        Movie movie = showtime.getMovie();
        Theater theater = showtime.getTheater();
        Seat seat = reservation.getSeat();

        return ResponseEntity.status(201).body(new ReservationResponse(reservation,
            new ShowtimeResponse(showtime, new MovieResponse(movie), new TheaterResponse(theater)),
            new SeatResponse(seat)));
    }
}
