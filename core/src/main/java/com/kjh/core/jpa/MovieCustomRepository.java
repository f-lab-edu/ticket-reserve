package com.kjh.core.jpa;

import com.kjh.core.dto.MovieSearchCondition;
import com.kjh.core.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieCustomRepository {

    Page<Movie> findAllBySearchCondition(MovieSearchCondition searchCondition, Pageable pageable);
}
