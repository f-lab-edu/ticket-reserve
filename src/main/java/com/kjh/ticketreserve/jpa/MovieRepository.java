package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
