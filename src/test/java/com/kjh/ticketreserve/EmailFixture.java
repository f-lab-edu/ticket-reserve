package com.kjh.ticketreserve;

public class EmailFixture {

    private final String localPart;

    public EmailFixture(String localPart) {
        this.localPart = localPart;
    }

    public String value() {
        return localPart + "@test.com";
    }
}
