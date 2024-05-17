package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long>, TheaterCustomRepository {
}
