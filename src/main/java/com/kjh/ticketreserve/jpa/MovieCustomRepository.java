package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.MovieSearchCondition;
import com.kjh.ticketreserve.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieCustomRepository {

    Page<Movie> findAllBySearchCondition(MovieSearchCondition searchCondition, Pageable pageable);
}
