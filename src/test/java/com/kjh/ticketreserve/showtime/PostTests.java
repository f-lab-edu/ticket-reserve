package com.kjh.ticketreserve.showtime;

import com.kjh.ticketreserve.*;
import com.kjh.ticketreserve.annotation.AutoDomainSource;
import com.kjh.ticketreserve.annotation.WebTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("POST /admin/showtimes")
public class PostTests {

    @ParameterizedTest
    @AutoDomainSource
    void 상영시간표를_등록하면_아이디를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        LocalDateTime showDatetime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showDatetime);

        // Act
        ResponseEntity<ShowtimeResponse> response = postWithToken(client,
            accessToken,
            "/admin/showtimes",
            showtimeRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).hasFieldOrProperty("id");
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_영화_아이디를_사용해_등록하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        long movieId,
        TheaterRequest theaterRequest,
        LocalDateTime showDatetime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showDatetime);

        // Act
        ResponseEntity<ShowtimeResponse> response = postWithToken(client,
            accessToken,
            "/admin/showtimes",
            showtimeRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_영화관_아이디를_사용해_등록하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        long theaterId,
        LocalDateTime showDatetime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showDatetime);

        // Act
        ResponseEntity<ShowtimeResponse> response = postWithToken(client,
            accessToken,
            "/admin/showtimes",
            showtimeRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 이미_존재하는_영화_영화관_상영시간을_사용해_등록하면_Bad_Request_상태코드를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        LocalDateTime showDatetime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showDatetime);
        createShowtime(client, accessToken, showtimeRequest);

        // Act
        ResponseEntity<ShowtimeResponse> response = postWithToken(client,
            accessToken,
            "/admin/showtimes",
            showtimeRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
