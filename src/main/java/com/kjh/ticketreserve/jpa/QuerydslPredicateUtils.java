package com.kjh.ticketreserve.jpa;

import com.querydsl.core.types.Predicate;

import java.util.function.Function;

public class QuerydslPredicateUtils {

    public static <T> Predicate ifNullNone(Function<T, Predicate> expression, T value) {
        if (value == null) {
            return null;
        }
        return expression.apply(value);
    }
}
