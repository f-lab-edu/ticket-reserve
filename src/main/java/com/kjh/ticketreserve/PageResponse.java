package com.kjh.ticketreserve;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(int totalPages, long totalElements, int numberOfElements, List<T> content) {

    public <S> PageResponse(Page<S> page, Function<S, T> mapper) {
        this(page.getTotalPages(),
            page.getTotalElements(),
            page.getNumberOfElements(),
            page.getContent().stream().map(mapper).toList());
    }
}
