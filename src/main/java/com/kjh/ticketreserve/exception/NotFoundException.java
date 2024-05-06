package com.kjh.ticketreserve.exception;

import org.springframework.http.HttpStatus;

public enum NotFoundException {
    NOT_FOUND(HttpStatus.NOT_FOUND, "데이터를 찾을 수 없습니다.");

    private final ResponseException responseException;

    NotFoundException(HttpStatus httpStatus, String message) {
        this.responseException = new ResponseException(httpStatus, message);
    }

    public ResponseException get() {
        return responseException;
    }
}
