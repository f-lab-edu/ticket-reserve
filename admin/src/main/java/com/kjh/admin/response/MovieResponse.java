package com.kjh.admin.response;

import com.kjh.core.model.Movie;

import java.time.LocalDateTime;

public record MovieResponse(long id, String title, LocalDateTime startDate, LocalDateTime endDate, int price) {

    public MovieResponse(Movie movie) {
        this(movie.getId(), movie.getTitle(), movie.getStartDate(), movie.getEndDate(), movie.getPrice());
    }
}
