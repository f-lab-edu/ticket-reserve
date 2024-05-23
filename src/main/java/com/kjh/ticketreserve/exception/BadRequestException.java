package com.kjh.ticketreserve.exception;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public enum BadRequestException implements Supplier<ResponseException> {
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    DUPLICATED_SHOWTIME(HttpStatus.BAD_REQUEST, "이미 존재하는 상영시간표입니다."),
    DUPLICATED_SEAT(HttpStatus.BAD_REQUEST, "이미 존재하는 좌석입니다."),
    ALREADY_RESERVED_SEAT(HttpStatus.BAD_REQUEST, "이미 예매된 좌석입니다."),
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 맞지 않습니다.");

    private final ResponseException responseException;

    BadRequestException(HttpStatus httpStatus, String message) {
        this.responseException = new ResponseException(httpStatus, message);
    }

    @Override
    public ResponseException get() {
        return responseException;
    }
}
