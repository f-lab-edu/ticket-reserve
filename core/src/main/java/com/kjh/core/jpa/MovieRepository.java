package com.kjh.core.jpa;

import com.kjh.core.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieCustomRepository {
}
