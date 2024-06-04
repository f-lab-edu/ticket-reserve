package com.kjh.core.jpa;

import com.kjh.core.model.Seat;
import com.kjh.core.model.SeatRowCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    boolean existsByTheaterIdAndRowCodeAndNumber(Long theaterId, SeatRowCode rowCode, int number);

    List<Seat> findByTheaterId(Long theaterId);

    List<Seat> findByTheaterIdOrderByRowCodeAscNumberAsc(Long theaterId);
}
