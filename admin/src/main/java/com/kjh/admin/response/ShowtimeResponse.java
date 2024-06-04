package com.kjh.admin.response;

import com.kjh.core.model.Showtime;

import java.time.LocalDateTime;

public record ShowtimeResponse(long id, MovieResponse movie, TheaterResponse theater, LocalDateTime showDatetime) {

    public ShowtimeResponse(Showtime showtime, MovieResponse movie, TheaterResponse theater) {
        this(showtime.getId(), movie, theater, showtime.getShowDatetime());
    }
}
