package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.*;
import com.kjh.ticketreserve.model.Movie;
import com.kjh.ticketreserve.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/admin/movies")
    public ResponseEntity<MovieResponse> createMovie(@RequestBody MovieRequest movieRequest) {
        Movie movie = movieService.createMovie(movieRequest.title(),
            movieRequest.startDate(),
            movieRequest.endDate(),
            movieRequest.price());

        return ResponseEntity.status(201).body(new MovieResponse(movie));
    }

    @GetMapping("/admin/movies/{id}")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable long id) {
        Movie movie = movieService.getMovie(id);
        return ResponseEntity.status(200).body(new MovieResponse(movie));
    }
    
    @PutMapping("/admin/movies/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable long id,
                                                     @RequestBody MovieRequest movieRequest) {
        Movie movie = movieService.updateMovie(id,
            movieRequest.title(),
            movieRequest.startDate(),
            movieRequest.endDate(),
            movieRequest.price());

        return ResponseEntity.status(200).body(new MovieResponse(movie));
    }

    @DeleteMapping("/admin/movies/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/movies")
    public ResponseEntity<PageResponse<MovieResponse>> searchMovies(
        @RequestParam int pageNumber,
        @RequestParam int pageSize,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) LocalDateTime screeningDate
    ) {
        Page<Movie> moviePage = movieService.searchMovies(pageNumber, pageSize, title, screeningDate);
        return ResponseEntity.status(200).body(new PageResponse<>(moviePage, m -> new MovieResponse(m)));
    }
}
