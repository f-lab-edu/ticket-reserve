package com.kjh.ticketreserve;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record ShowtimeUpdateRequest(LocalDateTime showDatetime) {

    public ShowtimeUpdateRequest(LocalDateTime showDatetime) {
        this.showDatetime = showDatetime.truncatedTo(ChronoUnit.SECONDS);
    }
}
