package com.kjh.core.dto;

import java.time.LocalDateTime;

public record MovieSearchCondition(String title, LocalDateTime screeningDate) {
}
