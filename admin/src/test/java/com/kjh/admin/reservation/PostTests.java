package com.kjh.admin.reservation;

import com.kjh.admin.annotation.AutoDomainSource;
import com.kjh.admin.annotation.WebTest;
import com.kjh.admin.request.*;
import com.kjh.admin.response.ReservationResponse;
import com.kjh.admin.service.MessageQueueServiceSpy;
import com.kjh.core.dto.MailMessage;
import com.kjh.core.service.Queue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.kjh.admin.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("POST /reservations")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PostTests {

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
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long seatId = createSeat(client, accessToken, theaterId, seatRequest);
        long showtimeId = createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showDatetime));

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
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long seatId = createSeat(client, accessToken, theaterId, seatRequest);
        long showtimeId = createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showDatetime));
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
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long seatId = createSeat(client, accessToken, theaterId, seatRequest);
        long showtimeId = createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showDatetime));
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

    @ParameterizedTest
    @AutoDomainSource
    void 예매_가능한_좌석을_예매하면_메일_메시지_큐를_전송한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        SeatRequest seatRequest,
        LocalDateTime showDatetime,
        @Autowired TestRestTemplate client,
        @Autowired MessageQueueServiceSpy messageQueueService
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long seatId = createSeat(client, accessToken, theaterId, seatRequest);
        long showtimeId = createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showDatetime));

        // Act
        ReservationRequest reservationRequest = new ReservationRequest(showtimeId, seatId);
        postWithToken(client,
            accessToken,
            "/reservations",
            reservationRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        Iterable<MessageQueueServiceSpy.Record> records = messageQueueService.getRecords();
        assertThat(records).hasSize(1);
        assertThat(records.iterator().next().queueName()).isEqualTo(Queue.MAIL);
        assertThat(records.iterator().next().object()).isInstanceOf(MailMessage.class);
        MailMessage mailMessage = (MailMessage) records.iterator().next().object();
        assertThat(mailMessage.email()).isEqualTo(credentials.email());
    }
}
