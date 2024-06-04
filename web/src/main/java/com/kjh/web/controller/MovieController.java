package com.kjh.web.controller;

import com.kjh.core.model.Movie;
import com.kjh.core.response.PageResponse;
import com.kjh.core.service.MovieService;
import com.kjh.web.response.MovieResponse;
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

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable long id) {
        Movie movie = movieService.getMovie(id);
        return ResponseEntity.status(200).body(new MovieResponse(movie));
    }

    @GetMapping("/movies")
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
