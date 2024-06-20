package com.kjh.web.reservation;

import com.kjh.web.MovieRequest;
import com.kjh.web.SeatRequest;
import com.kjh.web.TestDataService;
import com.kjh.web.TheaterRequest;
import com.kjh.web.annotation.AutoDomainSource;
import com.kjh.web.annotation.WebTest;
import com.kjh.web.request.Credentials;
import com.kjh.web.request.ReservationRequest;
import com.kjh.web.response.ReservationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.kjh.web.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("POST /reservations")
class PostTests {

    @Autowired
    private TestDataService testDataService;

    @ParameterizedTest
    @AutoDomainSource
    void 예매_가능한_좌석을_예매하면_OK_상태코드를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        SeatRequest seatRequest,
        LocalDateTime showDatetime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = testDataService.createMovie(movieRequest).getId();
        long theaterId = testDataService.createTheater(theaterRequest).getId();
        long seatId = testDataService.createSeat(theaterId, seatRequest).getId();
        long showtimeId = testDataService.createShowtime(movieId, theaterId, showDatetime).getId();

        // Act
        ReservationRequest reservationRequest = new ReservationRequest(showtimeId, seatId);
        ResponseEntity<ReservationResponse> response = postWithToken(client,
            accessToken,
            "/reservations",
            reservationRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).hasFieldOrProperty("id");
    }

    @ParameterizedTest
    @AutoDomainSource
    void 이미_예매된_좌석을_예매하면_Bad_Request_상태코드를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        SeatRequest seatRequest,
        LocalDateTime showDatetime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = testDataService.createMovie(movieRequest).getId();
        long theaterId = testDataService.createTheater(theaterRequest).getId();
        long seatId = testDataService.createSeat(theaterId, seatRequest).getId();
        long showtimeId = testDataService.createShowtime(movieId, theaterId, showDatetime).getId();
        ReservationRequest reservationRequest = new ReservationRequest(showtimeId, seatId);
        createReservation(client, accessToken, reservationRequest);

        // Act
        ResponseEntity<ReservationResponse> response = postWithToken(client,
            accessToken,
            "/reservations",
            reservationRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 동시에_동일한_좌석을_예매해도_한_번만_예매에_성공한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        SeatRequest seatRequest,
        LocalDateTime showDatetime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = testDataService.createMovie(movieRequest).getId();
        long theaterId = testDataService.createTheater(theaterRequest).getId();
        long seatId = testDataService.createSeat(theaterId, seatRequest).getId();
        long showtimeId = testDataService.createShowtime(movieId, theaterId, showDatetime).getId();
        ReservationRequest reservationRequest = new ReservationRequest(showtimeId, seatId);

        // Act
        int numberOfThreads = 10;
        int numberOfCalls = 10;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        List<CompletableFuture<ResponseEntity<ReservationResponse>>> futures = new ArrayList<>();
        for (int i = 0; i < numberOfCalls; i++) {
            futures.add(
                CompletableFuture.supplyAsync(() -> postWithToken(client,
                    accessToken,
                    "/reservations",
                    reservationRequest,
                    new ParameterizedTypeReference<>() {}), service)
            );
        }
        List<ResponseEntity<ReservationResponse>> responses = futures.stream()
            .map(CompletableFuture::join)
            .toList();

        // Assert
        Map<Integer, List<ResponseEntity<ReservationResponse>>> resultsByStatusCode = responses.stream().
            collect(Collectors.groupingBy(r -> r.getStatusCode().value()));
        assertThat(resultsByStatusCode.get(201).size()).isEqualTo(1);
        assertThat(resultsByStatusCode.get(400).size()).isEqualTo(numberOfCalls - 1);
    }
}
