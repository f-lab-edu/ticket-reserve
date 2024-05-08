package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.MovieSearchCondition;
import com.kjh.ticketreserve.model.Movie;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.kjh.ticketreserve.jpa.QuerydslPredicateUtils.ifNullNone;
import static com.kjh.ticketreserve.model.QMovie.movie;

@RequiredArgsConstructor
public class MovieCustomRepositoryImpl implements MovieCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Movie> findAllBySearchCondition(MovieSearchCondition searchCondition, Pageable pageable) {
        JPAQuery<Movie> query = queryFactory
            .select(movie)
            .from(movie)
            .where(
                ifNullNone(movie.title::contains, searchCondition.title()),
                ifNullNone(movie.startDate::loe, searchCondition.screeningDate()),
                ifNullNone(movie.endDate::goe, searchCondition.screeningDate())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory
            .select(Wildcard.count)
            .from(movie)
            .where(
                ifNullNone(movie.title::contains, searchCondition.title()),
                ifNullNone(movie.startDate::loe, searchCondition.screeningDate()),
                ifNullNone(movie.endDate::goe, searchCondition.screeningDate())
            );

        return new PageImpl<>(query.fetch(), pageable, countQuery.fetch().get(0));
    }
}
