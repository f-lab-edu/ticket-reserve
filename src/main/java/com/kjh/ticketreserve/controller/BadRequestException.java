package com.kjh.ticketreserve.controller;

import org.springframework.http.HttpStatus;

public enum BadRequestException {
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 맞지 않습니다.");

    private final ResponseException responseException;

    BadRequestException(HttpStatus httpStatus, String message) {
        this.responseException = new ResponseException(httpStatus, message);
    }

    public ResponseException get() {
        return responseException;
    }
}
