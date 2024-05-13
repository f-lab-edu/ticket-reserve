package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.ShowtimeSearchCondition;
import com.kjh.ticketreserve.model.Showtime;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.kjh.ticketreserve.jpa.QuerydslPredicateUtils.ifNullNone;
import static com.kjh.ticketreserve.model.QMovie.movie;
import static com.kjh.ticketreserve.model.QShowtime.*;
import static com.kjh.ticketreserve.model.QTheater.theater;

@RequiredArgsConstructor
public class ShowtimeRepositoryImpl implements ShowtimeCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Showtime> findAllBySearchCondition(ShowtimeSearchCondition searchCondition, Pageable pageable) {
        JPAQuery<Showtime> query = queryFactory
            .select(showtime1)
            .from(showtime1)
            .join(showtime1.movie, movie).fetchJoin()
            .join(showtime1.theater, theater).fetchJoin()
            .where(
                ifNullNone(movie.id::eq, searchCondition.movieId()),
                ifNullNone(theater.id::eq, searchCondition.theaterId()),
                ifNullNone(showtime1.showtime::goe, searchCondition.showtimeFrom()),
                ifNullNone(showtime1.showtime::lt, searchCondition.showtimeTo())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory
            .select(Wildcard.count)
            .from(showtime1)
            .where(
                ifNullNone(showtime1.movie.id::eq, searchCondition.movieId()),
                ifNullNone(showtime1.theater.id::eq, searchCondition.theaterId()),
                ifNullNone(showtime1.showtime::goe, searchCondition.showtimeFrom()),
                ifNullNone(showtime1.showtime::lt, searchCondition.showtimeTo())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        return new PageImpl<>(query.fetch(), pageable, countQuery.fetch().get(0));
    }
}
