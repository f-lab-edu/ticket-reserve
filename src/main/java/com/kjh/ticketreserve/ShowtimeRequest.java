package com.kjh.ticketreserve;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record ShowtimeRequest(long movieId, long theaterId, LocalDateTime showDatetime) {

    public ShowtimeRequest(long movieId, long theaterId, LocalDateTime showDatetime) {
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.showDatetime = showDatetime.truncatedTo(ChronoUnit.SECONDS);
    }
}
