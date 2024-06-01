package com.kjh.core.jpa;

import com.kjh.core.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long>, TheaterCustomRepository {
}
