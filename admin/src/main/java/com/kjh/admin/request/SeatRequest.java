package com.kjh.admin.request;

import com.kjh.core.model.SeatRowCode;

public record SeatRequest(SeatRowCode rowCode, int number) {
}
