package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.MovieRequest;
import com.kjh.ticketreserve.MovieResponse;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.jpa.MovieRepository;
import com.kjh.ticketreserve.model.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostMapping("/admin/movie")
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

    @GetMapping("/admin/movie/{id}")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable("id") long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(BadRequestException.NOT_FOUND::get);
        return ResponseEntity.status(200).body(new MovieResponse(
            movie.getId(),
            movie.getTitle(),
            movie.getStartDate(),
            movie.getEndDate(),
            movie.getPrice()));
    }
}
