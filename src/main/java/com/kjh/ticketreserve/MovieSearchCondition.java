package com.kjh.ticketreserve;

import java.time.LocalDateTime;

public record MovieSearchCondition(String title, LocalDateTime screeningDate) {
}
