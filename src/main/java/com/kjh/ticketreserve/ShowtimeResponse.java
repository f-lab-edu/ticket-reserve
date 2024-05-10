package com.kjh.ticketreserve;

import java.time.LocalDateTime;

public record ShowtimeResponse(long id, MovieResponse movie, TheaterResponse theater, LocalDateTime showtime) {
}
