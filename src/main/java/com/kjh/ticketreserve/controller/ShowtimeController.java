package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.*;
import com.kjh.ticketreserve.model.Movie;
import com.kjh.ticketreserve.model.Showtime;
import com.kjh.ticketreserve.model.Theater;
import com.kjh.ticketreserve.service.MovieService;
import com.kjh.ticketreserve.service.ShowtimeService;
import com.kjh.ticketreserve.service.TheaterService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class ShowtimeController {

    private final ShowtimeService showtimeService;
    private final MovieService movieService;
    private final TheaterService theaterService;

    public ShowtimeController(ShowtimeService showtimeService,
                              MovieService movieService,
                              TheaterService theaterService) {
        this.showtimeService = showtimeService;
        this.movieService = movieService;
        this.theaterService = theaterService;
    }

    @PostMapping("/admin/showtimes")
    public ResponseEntity<ShowtimeResponse> createShowtime(@RequestBody ShowtimeRequest showtimeRequest) {
        Movie movie = movieService.getMovie(showtimeRequest.movieId());
        Theater theater = theaterService.getTheater(showtimeRequest.theaterId());
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater(theater);
        showtime.setShowDatetime(showtimeRequest.showDatetime());
        showtimeService.createShowtime(showtime);
        return ResponseEntity.status(201).body(new ShowtimeResponse(
            showtime.getId(),
            new MovieResponse(movie),
            new TheaterResponse(theater),
            showtime.getShowDatetime()));
    }

    @GetMapping("/showtimes/{id}")
    public ResponseEntity<ShowtimeResponse> getShowtime(@PathVariable long id) {
        Showtime showtime = showtimeService.getShowtime(id);
        return ResponseEntity.status(200).body(new ShowtimeResponse(
            showtime.getId(),
            new MovieResponse(showtime.getMovie()),
            new TheaterResponse(showtime.getTheater()),
            showtime.getShowDatetime()));
    }

    @PutMapping("/admin/showtimes/{id}")
    public ResponseEntity<ShowtimeResponse> updateShowtime(@PathVariable long id,
                                                           @RequestBody ShowtimeUpdateRequest showtimeUpdateRequest) {
        Showtime showtime = showtimeService.updateShowtime(id, showtimeUpdateRequest);
        return ResponseEntity.status(200).body(new ShowtimeResponse(
            showtime.getId(),
            new MovieResponse(showtime.getMovie()),
            new TheaterResponse(showtime.getTheater()),
            showtime.getShowDatetime()));
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
            s -> new ShowtimeResponse(s.getId(),
                new MovieResponse(s.getMovie()),
                new TheaterResponse(s.getTheater()),
                s.getShowDatetime())));
    }
}
