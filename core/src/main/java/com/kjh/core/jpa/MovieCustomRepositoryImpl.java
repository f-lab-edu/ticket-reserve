package com.kjh.core.jpa;

import com.kjh.core.dto.MovieSearchCondition;
import com.kjh.core.model.Movie;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.kjh.core.model.QMovie.movie;

@RequiredArgsConstructor
public class MovieCustomRepositoryImpl implements MovieCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Movie> findAllBySearchCondition(MovieSearchCondition searchCondition, Pageable pageable) {
        JPAQuery<Movie> query = queryFactory
            .select(movie)
            .from(movie)
            .where(
                QuerydslPredicateUtils.ifNullNone(movie.title::contains, searchCondition.title()),
                QuerydslPredicateUtils.ifNullNone(movie.startDate::loe, searchCondition.screeningDate()),
                QuerydslPredicateUtils.ifNullNone(movie.endDate::goe, searchCondition.screeningDate())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory
            .select(Wildcard.count)
            .from(movie)
            .where(
                QuerydslPredicateUtils.ifNullNone(movie.title::contains, searchCondition.title()),
                QuerydslPredicateUtils.ifNullNone(movie.startDate::loe, searchCondition.screeningDate()),
                QuerydslPredicateUtils.ifNullNone(movie.endDate::goe, searchCondition.screeningDate())
            );

        return new PageImpl<>(query.fetch(), pageable, countQuery.fetch().get(0));
    }
}
