package com.kjh.core.response;

import java.util.List;
import java.util.function.Function;

public record ArrayResponse<T>(List<T> contents) {

    public <S> ArrayResponse(List<S> list, Function<S, T> mapper) {
        this(list.stream().map(mapper).toList());
    }
}
