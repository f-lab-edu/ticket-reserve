package com.kjh.ticketreserve;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ShowtimeSearchCondition(Long movieId, Long theaterId, LocalDateTime showtimeFrom, LocalDateTime showtimeTo) {

    public ShowtimeSearchCondition(Long movieId, Long theaterId, LocalDate date) {
        this(movieId,
            theaterId,
            date == null ? null : date.atStartOfDay(),
            date == null ? null : date.plusDays(1).atStartOfDay());
    }
}
