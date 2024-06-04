package com.kjh.web;

import com.kjh.core.model.SeatRowCode;

public record SeatRequest(SeatRowCode rowCode, int number) {
}
