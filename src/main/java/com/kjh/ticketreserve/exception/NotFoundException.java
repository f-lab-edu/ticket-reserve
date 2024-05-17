package com.kjh.ticketreserve.exception;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public enum NotFoundException implements Supplier<ResponseException> {
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    NOT_FOUND_MOVIE(HttpStatus.NOT_FOUND, "영화를 찾을 수 없습니다."),
    NOT_FOUND_THEATER(HttpStatus.NOT_FOUND, "영화관을 찾을 수 없습니다."),
    NOT_FOUND_SHOWTIME(HttpStatus.NOT_FOUND, "상영시간표를 찾을 수 없습니다."),
    NOT_FOUND_SEAT(HttpStatus.NOT_FOUND, "좌석을 찾을 수 없습니다.");

    private final ResponseException responseException;

    NotFoundException(HttpStatus httpStatus, String message) {
        this.responseException = new ResponseException(httpStatus, message);
    }

    @Override
    public ResponseException get() {
        return responseException;
    }
}
