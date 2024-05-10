package com.kjh.ticketreserve;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record ShowtimeUpdateRequest(LocalDateTime showtime) {

    public ShowtimeUpdateRequest(LocalDateTime showtime) {
        this.showtime = showtime.truncatedTo(ChronoUnit.SECONDS);
    }
}
