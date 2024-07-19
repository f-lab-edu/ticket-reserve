package com.kjh.core.template;

import lombok.Getter;

@Getter
public enum TemplateCode {
    RESERVATION_CONFIRMED("reservation-confirmed", "영화가 예매되었습니다!");

    private final String filename;
    private final String subject;

    TemplateCode(String filename, String subject) {
        this.filename = filename;
        this.subject = subject;
    }
}
