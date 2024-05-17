package com.kjh.ticketreserve.service;

import com.kjh.ticketreserve.MovieRequest;
import com.kjh.ticketreserve.MovieSearchCondition;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.MovieRepository;
import com.kjh.ticketreserve.model.Movie;
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

    @Transactional(readOnly = true)
    public Movie getMovie(long id) {
        return movieRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_MOVIE);
    }

    @Transactional
    public Movie updateMovie(long id, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND_MOVIE);
        movie.setTitle(movieRequest.title());
        movie.setStartDate(movieRequest.startDate());
        movie.setEndDate(movieRequest.endDate());
        movie.setPrice(movieRequest.price());
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
