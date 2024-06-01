package com.kjh.core.jpa;

import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuerydslPredicateUtils {

    public static <T> Predicate ifNullNone(Function<T, Predicate> expression, T value) {
        if (value == null) {
            return null;
        }
        return expression.apply(value);
    }
}
