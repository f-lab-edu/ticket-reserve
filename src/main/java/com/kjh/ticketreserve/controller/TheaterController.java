package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.*;
import com.kjh.ticketreserve.model.Seat;
import com.kjh.ticketreserve.model.Theater;
import com.kjh.ticketreserve.service.TheaterService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @PostMapping("/admin/theaters")
    public ResponseEntity<TheaterResponse> createTheater(@RequestBody TheaterRequest theaterRequest) {
        Theater theater = new Theater();
        theater.setName(theaterRequest.name());
        theater.setAddress(theaterRequest.address());
        theaterService.createTheater(theater);

        return ResponseEntity.status(201).body(new TheaterResponse(
            theater.getId(),
            theater.getName(),
            theater.getAddress()
        ));
    }

    @GetMapping("/theaters/{id}")
    public ResponseEntity<TheaterResponse> getTheater(@PathVariable Long id) {
        Theater theater = theaterService.getTheater(id);
        return ResponseEntity.status(200).body(new TheaterResponse(
            theater.getId(),
            theater.getName(),
            theater.getAddress()));
    }

    @PutMapping("/admin/theaters/{id}")
    public ResponseEntity<TheaterResponse> updateTheater(@PathVariable("id") Long id,
                                                         @RequestBody TheaterRequest theaterRequest) {
        Theater theater = theaterService.updateTheater(id, theaterRequest);
        return ResponseEntity.status(200).body(new TheaterResponse(
            theater.getId(),
            theater.getName(),
            theater.getAddress()));
    }

    @DeleteMapping("/admin/theaters/{id}")
    public ResponseEntity<TheaterResponse> deleteTheater(@PathVariable("id") Long id) {
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
        return ResponseEntity.status(200).body(new PageResponse<>(theaterPage,
            t -> new TheaterResponse(t.getId(), t.getName(), t.getAddress())));
    }

    @PostMapping("/admin/theaters/{theaterId}/seats")
    public ResponseEntity<SeatResponse> createSeat(@PathVariable Long theaterId, @RequestBody SeatRequest seatRequest) {
        Theater theater = theaterService.getTheater(theaterId);
        Seat seat = new Seat();
        seat.setTheater(theater);
        seat.setRowCode(seatRequest.rowCode());
        seat.setNumber(seatRequest.number());
        theaterService.createSeat(seat);
        return ResponseEntity.status(201).body(new SeatResponse(
            seat.getId(),
            seat.getRowCode(),
            seat.getNumber()));
    }

    @GetMapping("/theaters/{theaterId}/seats/{seatId}")
    public ResponseEntity<SeatResponse> getSeat(@PathVariable Long theaterId, @PathVariable Long seatId) {
        theaterService.getTheater(theaterId);
        Seat seat = theaterService.getSeat(seatId);
        return ResponseEntity.status(200).body(new SeatResponse(
            seat.getId(),
            seat.getRowCode(),
            seat.getNumber()));
    }
}
