package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.TheaterSearchCondition;
import com.kjh.ticketreserve.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TheaterCustomRepository {

    Page<Theater> findAllBySearchCondition(TheaterSearchCondition searchCondition, Pageable pageable);
}
