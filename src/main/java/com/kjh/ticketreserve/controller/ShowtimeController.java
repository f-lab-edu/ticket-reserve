package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.*;
import com.kjh.ticketreserve.model.Showtime;
import com.kjh.ticketreserve.model.ShowtimeSeat;
import com.kjh.ticketreserve.service.ShowtimeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @PostMapping("/admin/showtimes")
    public ResponseEntity<ShowtimeResponse> createShowtime(@RequestBody ShowtimeRequest showtimeRequest) {
        Showtime showtime = showtimeService.createShowtime(showtimeRequest.movieId(),
            showtimeRequest.theaterId(),
            showtimeRequest.showDatetime());
        return ResponseEntity.status(201).body(new ShowtimeResponse(
            showtime,
            new MovieResponse(showtime.getMovie()),
            new TheaterResponse(showtime.getTheater())));
    }

    @GetMapping("/showtimes/{id}")
    public ResponseEntity<ShowtimeResponse> getShowtime(@PathVariable long id) {
        Showtime showtime = showtimeService.getShowtime(id);
        return ResponseEntity.status(200).body(new ShowtimeResponse(
            showtime,
            new MovieResponse(showtime.getMovie()),
            new TheaterResponse(showtime.getTheater())));
    }

    @PutMapping("/admin/showtimes/{id}")
    public ResponseEntity<ShowtimeResponse> updateShowtime(@PathVariable long id,
                                                           @RequestBody ShowtimeUpdateRequest showtimeUpdateRequest) {
        Showtime showtime = showtimeService.updateShowtime(id, showtimeUpdateRequest.showDatetime());
        return ResponseEntity.status(200).body(new ShowtimeResponse(
            showtime,
            new MovieResponse(showtime.getMovie()),
            new TheaterResponse(showtime.getTheater())));
    }

    @DeleteMapping("/admin/showtimes/{id}")
    public ResponseEntity<Object> deleteShowtime(@PathVariable long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/showtimes")
    public ResponseEntity<PageResponse<ShowtimeResponse>> searchShowtimes(
        @RequestParam int pageNumber,
        @RequestParam int pageSize,
        @RequestParam(required = false) Long movieId,
        @RequestParam(required = false) Long theaterId,
        @RequestParam(required = false) LocalDate date
    ) {
        Page<Showtime> showtimePage = showtimeService.searchShowtimes(pageNumber, pageSize, movieId, theaterId, date);
        return ResponseEntity.status(200).body(new PageResponse<>(showtimePage,
            s -> new ShowtimeResponse(s, new MovieResponse(s.getMovie()), new TheaterResponse(s.getTheater()))));
    }

    @GetMapping("/showtimes/{id}/seats")
    public ResponseEntity<ArrayResponse<ShowtimeSeatResponse>> getSeats(@PathVariable long id) {
        List<ShowtimeSeat> showtimeSeats = showtimeService.getSeats(id);
        return ResponseEntity.status(200).body(new ArrayResponse<>(showtimeSeats,
            s -> new ShowtimeSeatResponse(s, s.getSeat())));
    }
}
