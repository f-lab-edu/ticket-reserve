package com.kjh.ticketreserve.service;

import com.kjh.ticketreserve.MovieRequest;
import com.kjh.ticketreserve.exception.NotFoundException;
import com.kjh.ticketreserve.jpa.MovieRepository;
import com.kjh.ticketreserve.model.Movie;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Movie updateMovie(long id, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND::get);
        movie.setTitle(movieRequest.title());
        movie.setStartDate(movieRequest.startDate());
        movie.setEndDate(movieRequest.endDate());
        movie.setPrice(movieRequest.price());
        return movie;
    }

    @Transactional
    public void deleteMovie(long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(NotFoundException.NOT_FOUND::get);
        movieRepository.delete(movie);
    }
}
