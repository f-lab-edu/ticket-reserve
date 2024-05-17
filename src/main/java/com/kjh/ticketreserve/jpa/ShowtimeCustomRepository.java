package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.ShowtimeSearchCondition;
import com.kjh.ticketreserve.model.Showtime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShowtimeCustomRepository {

    Page<Showtime> findAllBySearchCondition(ShowtimeSearchCondition searchCondition, Pageable pageable);
}
