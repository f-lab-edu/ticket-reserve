package com.kjh.ticketreserve.controller;

import com.kjh.ticketreserve.TheaterRequest;
import com.kjh.ticketreserve.TheaterResponse;
import com.kjh.ticketreserve.model.Theater;
import com.kjh.ticketreserve.service.TheaterService;
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
}
