package com.kjh.web.response;

import com.kjh.core.model.Theater;

public record TheaterResponse(long id, String name, String address) {

    public TheaterResponse(Theater theater) {
        this(theater.getId(), theater.getName(), theater.getAddress());
    }
}
