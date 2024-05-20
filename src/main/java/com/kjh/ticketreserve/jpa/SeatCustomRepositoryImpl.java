package com.kjh.ticketreserve.jpa;

import com.kjh.ticketreserve.ReservationStatus;
import com.kjh.ticketreserve.ShowtimeSeat;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.kjh.ticketreserve.model.QReservation.reservation;
import static com.kjh.ticketreserve.model.QSeat.seat;

@RequiredArgsConstructor
public class SeatCustomRepositoryImpl implements SeatCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ShowtimeSeat> findShowtimeSeats(Long theaterId, Long showtimeId) {
        return queryFactory
            .select(Projections.constructor(ShowtimeSeat.class,
                seat.id,
                seat.rowCode,
                seat.number,
                reservation.status
            ))
            .from(seat)
            .leftJoin(reservation)
            .on(
                seat.id.eq(reservation.seat.id)
                    .and(reservation.showtime.id.eq(showtimeId))
                    .and(reservation.status.eq(ReservationStatus.RESERVED))
            )
            .where(
                seat.theater.id.eq(theaterId)
            )
            .fetch();
    }
}
