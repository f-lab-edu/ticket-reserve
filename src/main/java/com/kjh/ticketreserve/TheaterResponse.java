package com.kjh.ticketreserve;

import com.kjh.ticketreserve.model.Theater;

public record TheaterResponse(long id, String name, String address) {

    public TheaterResponse(Theater theater) {
        this(theater.getId(), theater.getName(), theater.getAddress());
    }
}
