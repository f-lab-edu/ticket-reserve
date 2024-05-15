package com.kjh.ticketreserve.showtime;

import com.kjh.ticketreserve.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("GET /showtimes")
public class GetTests {

    @ParameterizedTest
    @AutoDomainSource
    void 상영시간표_아이디를_사용해_조회하면_상영시간표_정보를_반환한다(
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
        long id = createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showDatetime));

        // Act
        ResponseEntity<ShowtimeResponse> response = getWithToken(client,
            accessToken,
            "/showtimes/" + id,
            ShowtimeResponse.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        ShowtimeResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(id);
        assertThat(body.movie().id()).isEqualTo(movieId);
        assertThat(body.theater().id()).isEqualTo(theaterId);
        assertThat(body.showDatetime()).isEqualToIgnoringNanos(showDatetime);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 상영시간표를_검색하면_상영시간표_리스트를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        List<LocalDateTime> showDatetimes,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        for (LocalDateTime showtime : showDatetimes) {
            createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showtime));
        }

        // Act
        ResponseEntity<PageResponse<ShowtimeResponse>> response = getWithToken(client,
            accessToken,
            "/showtimes?pageNumber=0&pageSize=" + showDatetimes.size(),
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content().size()).isEqualTo(showDatetimes.size());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 영화_아이디를_사용해_검색하면_조건에_맞는_상영시간표_리스트를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        List<LocalDateTime> showDatetimes,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        List<ShowtimeRequest> showtimeRequests = new ArrayList<>();
        for (LocalDateTime showtime : showDatetimes) {
            long movieId = createMovie(client, accessToken, movieRequest);
            long theaterId = createTheater(client, accessToken, theaterRequest);
            ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showtime);
            createShowtime(client, accessToken, showtimeRequest);
            showtimeRequests.add(showtimeRequest);
        }

        // Act
        ShowtimeRequest showtimeToFind = showtimeRequests.get(0);
        ResponseEntity<PageResponse<ShowtimeResponse>> response = getWithToken(client,
            accessToken,
            "/showtimes?pageNumber=0&pageSize=" + showDatetimes.size() + "&movieId=" + showtimeToFind.movieId(),
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content().size()).isEqualTo(1);
        ShowtimeResponse found = response.getBody().content().get(0);
        assertThat(found.movie().id()).isEqualTo(showtimeToFind.movieId());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 영화관_아이디를_사용해_검색하면_조건에_맞는_상영시간표_리스트를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        List<LocalDateTime> showDatetimes,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        List<ShowtimeRequest> showtimeRequests = new ArrayList<>();
        for (LocalDateTime showtime : showDatetimes) {
            long movieId = createMovie(client, accessToken, movieRequest);
            long theaterId = createTheater(client, accessToken, theaterRequest);
            ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showtime);
            createShowtime(client, accessToken, showtimeRequest);
            showtimeRequests.add(showtimeRequest);
        }

        // Act
        ShowtimeRequest showtimeToFind = showtimeRequests.get(0);
        ResponseEntity<PageResponse<ShowtimeResponse>> response = getWithToken(client,
            accessToken,
            "/showtimes?pageNumber=0&pageSize=" + showDatetimes.size() + "&theaterId=" + showtimeToFind.theaterId(),
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content().size()).isEqualTo(1);
        ShowtimeResponse found = response.getBody().content().get(0);
        assertThat(found.theater().id()).isEqualTo(showtimeToFind.theaterId());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 일자를_사용해_검색하면_조건에_맞는_상영시간표_리스트를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        List<LocalDateTime> showDatetimes,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        List<ShowtimeRequest> showtimeRequests = new ArrayList<>();
        for (LocalDateTime showtime : showDatetimes) {
            long movieId = createMovie(client, accessToken, movieRequest);
            long theaterId = createTheater(client, accessToken, theaterRequest);
            ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showtime);
            createShowtime(client, accessToken, showtimeRequest);
            showtimeRequests.add(showtimeRequest);
        }

        // Act
        ShowtimeRequest showtimeToFind = showtimeRequests.get(0);
        ResponseEntity<PageResponse<ShowtimeResponse>> response = getWithToken(client,
            accessToken,
            "/showtimes?pageNumber=0&pageSize=" + showDatetimes.size() +
                "&date=" + showtimeToFind.showDatetime().toLocalDate(),
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content().size()).isEqualTo(1);
        ShowtimeResponse found = response.getBody().content().get(0);
        assertThat(found.showDatetime()).isEqualTo(showtimeToFind.showDatetime());
    }
}
