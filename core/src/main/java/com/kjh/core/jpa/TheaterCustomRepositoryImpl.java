package com.kjh.core.jpa;

import com.kjh.core.dto.TheaterSearchCondition;
import com.kjh.core.model.Theater;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.kjh.core.model.QTheater.theater;

@RequiredArgsConstructor
public class TheaterCustomRepositoryImpl implements TheaterCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Theater> findAllBySearchCondition(TheaterSearchCondition searchCondition, Pageable pageable) {
        JPAQuery<Theater> query = queryFactory
            .select(theater)
            .from(theater)
            .where(
                QuerydslPredicateUtils.ifNullNone(theater.name::contains, searchCondition.name()),
                QuerydslPredicateUtils.ifNullNone(theater.address::contains, searchCondition.address())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory
            .select(Wildcard.count)
            .from(theater)
            .where(
                QuerydslPredicateUtils.ifNullNone(theater.name::contains, searchCondition.name()),
                QuerydslPredicateUtils.ifNullNone(theater.address::contains, searchCondition.address())
            );

        return new PageImpl<>(query.fetch(), pageable, countQuery.fetch().get(0));
    }
}
