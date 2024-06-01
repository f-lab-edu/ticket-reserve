package com.kjh.admin.controller;

import com.kjh.admin.request.SeatRequest;
import com.kjh.admin.request.TheaterRequest;
import com.kjh.core.response.ArrayResponse;
import com.kjh.core.response.PageResponse;
import com.kjh.admin.response.SeatResponse;
import com.kjh.admin.response.TheaterResponse;
import com.kjh.core.model.Seat;
import com.kjh.core.model.Theater;
import com.kjh.core.service.TheaterService;
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

    @PostMapping("/theaters")
    public ResponseEntity<TheaterResponse> createTheater(@RequestBody TheaterRequest theaterRequest) {
        Theater theater = theaterService.createTheater(theaterRequest.name(), theaterRequest.address());
        return ResponseEntity.status(201).body(new TheaterResponse(theater));
    }

    @GetMapping("/theaters/{id}")
    public ResponseEntity<TheaterResponse> getTheater(@PathVariable Long id) {
        Theater theater = theaterService.getTheater(id);
        return ResponseEntity.status(200).body(new TheaterResponse(theater));
    }

    @PutMapping("/theaters/{id}")
    public ResponseEntity<TheaterResponse> updateTheater(@PathVariable Long id,
                                                         @RequestBody TheaterRequest theaterRequest) {
        Theater theater = theaterService.updateTheater(id, theaterRequest.name(), theaterRequest.address());
        return ResponseEntity.status(200).body(new TheaterResponse(theater));
    }

    @DeleteMapping("/theaters/{id}")
    public ResponseEntity<TheaterResponse> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.ok().build();
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

    @PostMapping("/theaters/{theaterId}/seats")
    public ResponseEntity<SeatResponse> createSeat(@PathVariable Long theaterId, @RequestBody SeatRequest seatRequest) {
        Seat seat = theaterService.createSeat(theaterId, seatRequest.rowCode(), seatRequest.number());
        return ResponseEntity.status(201).body(new SeatResponse(seat));
    }

    @GetMapping("/theaters/{theaterId}/seats/{seatId}")
    public ResponseEntity<SeatResponse> getSeat(@PathVariable Long theaterId, @PathVariable Long seatId) {
        theaterService.getTheater(theaterId);
        Seat seat = theaterService.getSeat(seatId);
        return ResponseEntity.status(200).body(new SeatResponse(seat));
    }

    @PutMapping("/theaters/{theaterId}/seats/{seatId}")
    public ResponseEntity<SeatResponse> updateSeat(@PathVariable Long theaterId,
                                                   @PathVariable Long seatId,
                                                   @RequestBody SeatRequest seatRequest) {
        theaterService.getTheater(theaterId);
        Seat seat = theaterService.updateSeat(seatId, seatRequest.rowCode(), seatRequest.number());
        return ResponseEntity.status(200).body(new SeatResponse(seat));
    }

    @DeleteMapping("/theaters/{theaterId}/seats/{seatId}")
    public ResponseEntity<Object> deleteSeat(@PathVariable Long theaterId, @PathVariable Long seatId) {
        theaterService.getTheater(theaterId);
        theaterService.deleteSeat(seatId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/theaters/{theaterId}/seats")
    public ResponseEntity<ArrayResponse<SeatResponse>> getSeats(@PathVariable Long theaterId) {
        theaterService.getTheater(theaterId);
        List<Seat> seats = theaterService.getSeats(theaterId);
        return ResponseEntity.status(200).body(new ArrayResponse<>(seats, s -> new SeatResponse(s)));
    }
}
