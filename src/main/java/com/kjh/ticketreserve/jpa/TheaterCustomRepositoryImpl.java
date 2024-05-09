package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.TheaterSearchCondition;
import com.kjh.ticketreserve.model.Theater;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.kjh.ticketreserve.jpa.QuerydslPredicateUtils.ifNullNone;
import static com.kjh.ticketreserve.model.QTheater.theater;

@RequiredArgsConstructor
public class TheaterCustomRepositoryImpl implements TheaterCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Theater> findAllBySearchCondition(TheaterSearchCondition searchCondition, Pageable pageable) {
        JPAQuery<Theater> query = queryFactory
            .select(theater)
            .from(theater)
            .where(
                ifNullNone(theater.name::contains, searchCondition.name()),
                ifNullNone(theater.address::contains, searchCondition.address())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory
            .select(Wildcard.count)
            .from(theater)
            .where(
                ifNullNone(theater.name::contains, searchCondition.name()),
                ifNullNone(theater.address::contains, searchCondition.address())
            );

        return new PageImpl<>(query.fetch(), pageable, countQuery.fetch().get(0));
    }
}
