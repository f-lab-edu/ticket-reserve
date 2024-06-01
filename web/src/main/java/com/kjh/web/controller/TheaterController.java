package com.kjh.web.controller;

import com.kjh.core.model.Seat;
import com.kjh.core.model.Theater;
import com.kjh.core.response.ArrayResponse;
import com.kjh.core.response.PageResponse;
import com.kjh.core.service.TheaterService;
import com.kjh.web.response.SeatResponse;
import com.kjh.web.response.TheaterResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping("/theaters/{id}")
    public ResponseEntity<TheaterResponse> getTheater(@PathVariable Long id) {
        Theater theater = theaterService.getTheater(id);
        return ResponseEntity.status(200).body(new TheaterResponse(theater));
    }

    @GetMapping("/theaters")
    public ResponseEntity<PageResponse<TheaterResponse>> searchTheaters(
        @RequestParam int pageNumber,
        @RequestParam int pageSize,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String address
    ) {
        Page<Theater> theaterPage = theaterService.searchTheaters(pageNumber, pageSize, name, address);
        return ResponseEntity.status(200).body(new PageResponse<>(theaterPage, t -> new TheaterResponse(t)));
    }

    @GetMapping("/theaters/{theaterId}/seats/{seatId}")
    public ResponseEntity<SeatResponse> getSeat(@PathVariable Long theaterId, @PathVariable Long seatId) {
        theaterService.getTheater(theaterId);
        Seat seat = theaterService.getSeat(seatId);
        return ResponseEntity.status(200).body(new SeatResponse(seat));
    }

    @GetMapping("/theaters/{theaterId}/seats")
    public ResponseEntity<ArrayResponse<SeatResponse>> getSeats(@PathVariable Long theaterId) {
        theaterService.getTheater(theaterId);
        List<Seat> seats = theaterService.getSeats(theaterId);
        return ResponseEntity.status(200).body(new ArrayResponse<>(seats, s -> new SeatResponse(s)));
    }
}
