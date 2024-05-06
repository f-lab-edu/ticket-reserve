package com.kjh.ticketreserve;

import java.time.LocalDateTime;

public record MovieResponse(long id, String title, LocalDateTime startDate, LocalDateTime endDate, int price) {
}
