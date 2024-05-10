package com.kjh.ticketreserve;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record ShowtimeRequest(long movieId, long theaterId, LocalDateTime showtime) {

    public ShowtimeRequest(long movieId, long theaterId, LocalDateTime showtime) {
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.showtime = showtime.truncatedTo(ChronoUnit.SECONDS);
    }
}
