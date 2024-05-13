package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.*;
import com.kjh.ticketreserve.jpa.MovieRepository;
import com.kjh.ticketreserve.model.Movie;
import com.kjh.ticketreserve.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class MovieController {

    private final MovieRepository movieRepository;
    private final MovieService movieService;

    public MovieController(MovieRepository movieRepository, MovieService movieService) {
        this.movieRepository = movieRepository;
        this.movieService = movieService;
    }

    @PostMapping("/admin/movies")
    public ResponseEntity<MovieResponse> createMovie(@RequestBody MovieRequest movieRequest) {
        Movie movie = new Movie();
        movie.setTitle(movieRequest.title());
        movie.setStartDate(movieRequest.startDate());
        movie.setEndDate(movieRequest.endDate());
        movie.setPrice(movieRequest.price());
        movieRepository.save(movie);

        return ResponseEntity.status(201).body(new MovieResponse(
            movie.getId(),
            movie.getTitle(),
            movie.getStartDate(),
            movie.getEndDate(),
            movie.getPrice()));
    }

    @GetMapping("/admin/movies/{id}")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable("id") long id) {
        Movie movie = movieService.getMovie(id);
        return ResponseEntity.status(200).body(new MovieResponse(
            movie.getId(),
            movie.getTitle(),
            movie.getStartDate(),
            movie.getEndDate(),
            movie.getPrice()));
    }
    
    @PutMapping("/admin/movies/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable("id") long id,
                                                     @RequestBody MovieRequest movieRequest) {
        Movie movie = movieService.updateMovie(id, movieRequest);
        return ResponseEntity.status(200).body(new MovieResponse(
            movie.getId(),
            movie.getTitle(),
            movie.getStartDate(),
            movie.getEndDate(),
            movie.getPrice()));
    }

    @DeleteMapping("/admin/movies/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable("id") long id) {
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
        return ResponseEntity.status(200).body(new PageResponse<>(moviePage,
            m -> new MovieResponse(m.getId(), m.getTitle(), m.getStartDate(), m.getEndDate(), m.getPrice())));
    }
}
