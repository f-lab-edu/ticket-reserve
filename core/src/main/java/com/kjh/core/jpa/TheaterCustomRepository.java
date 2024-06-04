package com.kjh.core.jpa;

import com.kjh.core.dto.TheaterSearchCondition;
import com.kjh.core.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TheaterCustomRepository {

    Page<Theater> findAllBySearchCondition(TheaterSearchCondition searchCondition, Pageable pageable);
}
