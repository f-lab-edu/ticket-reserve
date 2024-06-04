package com.kjh.core.jpa;

import com.kjh.core.dto.ShowtimeSearchCondition;
import com.kjh.core.model.Showtime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShowtimeCustomRepository {

    Page<Showtime> findAllBySearchCondition(ShowtimeSearchCondition searchCondition, Pageable pageable);
}
