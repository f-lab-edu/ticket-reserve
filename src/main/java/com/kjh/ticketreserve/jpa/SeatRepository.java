package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.SeatRowCode;
import com.kjh.ticketreserve.model.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long>, SeatCustomRepository {

    boolean existsByTheaterIdAndRowCodeAndNumber(Long theaterId, SeatRowCode rowCode, int number);

    List<Seat> findByTheaterIdOrderByRowCodeAscNumberAsc(Long theaterId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Seat s where s.id = :id")
    Optional<Seat> findByIdWithLock(@Param("id") Long id);
}
