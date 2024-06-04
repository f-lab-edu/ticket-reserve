package com.kjh.core.jpa;

import com.kjh.core.dto.ShowtimeSearchCondition;
import com.kjh.core.model.Showtime;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.kjh.core.model.QMovie.movie;
import static com.kjh.core.model.QShowtime.showtime;
import static com.kjh.core.model.QTheater.theater;

@RequiredArgsConstructor
public class ShowtimeRepositoryImpl implements ShowtimeCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Showtime> findAllBySearchCondition(ShowtimeSearchCondition searchCondition, Pageable pageable) {
        JPAQuery<Showtime> query = queryFactory
            .select(showtime)
            .from(showtime)
            .join(showtime.movie, movie).fetchJoin()
            .join(showtime.theater, theater).fetchJoin()
            .where(
                QuerydslPredicateUtils.ifNullNone(movie.id::eq, searchCondition.movieId()),
                QuerydslPredicateUtils.ifNullNone(theater.id::eq, searchCondition.theaterId()),
                QuerydslPredicateUtils.ifNullNone(showtime.showDatetime::goe, searchCondition.showDatetimeFrom()),
                QuerydslPredicateUtils.ifNullNone(showtime.showDatetime::lt, searchCondition.showDatetimeTo())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory
            .select(Wildcard.count)
            .from(showtime)
            .where(
                QuerydslPredicateUtils.ifNullNone(showtime.movie.id::eq, searchCondition.movieId()),
                QuerydslPredicateUtils.ifNullNone(showtime.theater.id::eq, searchCondition.theaterId()),
                QuerydslPredicateUtils.ifNullNone(showtime.showDatetime::goe, searchCondition.showDatetimeFrom()),
                QuerydslPredicateUtils.ifNullNone(showtime.showDatetime::lt, searchCondition.showDatetimeTo())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        return new PageImpl<>(query.fetch(), pageable, countQuery.fetch().get(0));
    }
}
