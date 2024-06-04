package com.kjh.core.service;

import com.kjh.core.dto.MovieSearchCondition;
import com.kjh.core.exception.NotFoundException;
import com.kjh.core.jpa.MovieRepository;
import com.kjh.core.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Movie createMovie(String title, LocalDateTime startDate, LocalDateTime endDate, int price) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setStartDate(startDate);
        movie.setEndDate(endDate);
        movie.setPrice(price);
        return movieRepository.save(movie);
    }

    @Transactional(readOnly = true)
    public Movie getMovie(long id) {
        return movieRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_MOVIE);
    }

    @Transactional
    public Movie updateMovie(long id, String title, LocalDateTime startDate, LocalDateTime endDate, int price) {
        Movie movie = movieRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_MOVIE);
        movie.setTitle(title);
        movie.setStartDate(startDate);
        movie.setEndDate(endDate);
        movie.setPrice(price);
        return movie;
    }

    @Transactional
    public void deleteMovie(long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_MOVIE);
        movieRepository.delete(movie);
    }

    @Transactional(readOnly = true)
    public Page<Movie> searchMovies(int pageNumber, int pageSize, String title, LocalDateTime screeningDate) {
        MovieSearchCondition searchCondition = new MovieSearchCondition(title, screeningDate);
        return movieRepository.findAllBySearchCondition(searchCondition, PageRequest.of(pageNumber, pageSize));
    }
}
