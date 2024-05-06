package com.kjh.ticketreserve;

import java.time.LocalDateTime;

public record MovieRequest(String title, LocalDateTime startDate, LocalDateTime endDate, int price) {
}
