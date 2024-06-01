package com.kjh.web.controller;

import com.kjh.core.model.Showtime;
import com.kjh.core.model.ShowtimeSeat;
import com.kjh.core.response.ArrayResponse;
import com.kjh.core.response.PageResponse;
import com.kjh.core.service.ShowtimeService;
import com.kjh.web.response.MovieResponse;
import com.kjh.web.response.ShowtimeResponse;
import com.kjh.web.response.ShowtimeSeatResponse;
import com.kjh.web.response.TheaterResponse;
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

    @GetMapping("/showtimes/{id}")
    public ResponseEntity<ShowtimeResponse> getShowtime(@PathVariable long id) {
        Showtime showtime = showtimeService.getShowtime(id);
        return ResponseEntity.status(200).body(new ShowtimeResponse(
            showtime,
            new MovieResponse(showtime.getMovie()),
            new TheaterResponse(showtime.getTheater())));
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
