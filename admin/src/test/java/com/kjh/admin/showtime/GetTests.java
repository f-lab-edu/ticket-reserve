package com.kjh.admin.showtime;

import com.kjh.admin.annotation.AutoDomainSource;
import com.kjh.admin.annotation.WebTest;
import com.kjh.admin.request.*;
import com.kjh.admin.response.ShowtimeResponse;
import com.kjh.admin.response.ShowtimeSeatResponse;
import com.kjh.core.response.ArrayResponse;
import com.kjh.core.response.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.kjh.admin.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("GET /showtimes")
class GetTests {

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
        for (LocalDateTime showDatetime : showDatetimes) {
            createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showDatetime));
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
        for (LocalDateTime showDatetime : showDatetimes) {
            long movieId = createMovie(client, accessToken, movieRequest);
            long theaterId = createTheater(client, accessToken, theaterRequest);
            ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showDatetime);
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
        for (LocalDateTime showDatetime : showDatetimes) {
            long movieId = createMovie(client, accessToken, movieRequest);
            long theaterId = createTheater(client, accessToken, theaterRequest);
            ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showDatetime);
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
        for (LocalDateTime showDatetime : showDatetimes) {
            long movieId = createMovie(client, accessToken, movieRequest);
            long theaterId = createTheater(client, accessToken, theaterRequest);
            ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showDatetime);
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

    @ParameterizedTest
    @AutoDomainSource
    void 상영시간표_아이디를_사용해_좌석을_조회하면_좌석_리스트를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        List<SeatRequest> seatRequests,
        TheaterRequest otherTheaterRequest,
        List<SeatRequest> otherSeatRequests,
        List<LocalDateTime> showDatetimes,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        for (SeatRequest seatRequest : seatRequests) {
            createSeat(client, accessToken, theaterId, seatRequest);
        }
        List<Long> showtimeIds = new ArrayList<>();
        for (LocalDateTime showDatetime : showDatetimes) {
            ShowtimeRequest showtimeRequest = new ShowtimeRequest(movieId, theaterId, showDatetime);
            long showtimeId = createShowtime(client, accessToken, showtimeRequest);
            showtimeIds.add(showtimeId);
        }

        long otherTheaterId = createTheater(client, accessToken, otherTheaterRequest);
        for (SeatRequest otherSeatRequest : otherSeatRequests) {
            createSeat(client, accessToken, otherTheaterId, otherSeatRequest);
        }
        for (LocalDateTime showDatetime : showDatetimes) {
            createShowtime(client, accessToken, new ShowtimeRequest(movieId, otherTheaterId, showDatetime));
        }

        // Act
        Long showtimeIdToFind = showtimeIds.get(0);
        ResponseEntity<ArrayResponse<ShowtimeSeatResponse>> response = getWithToken(client,
            accessToken,
            "/showtimes/" + showtimeIdToFind + "/seats",
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        ArrayResponse<ShowtimeSeatResponse> body = response.getBody();
        assertThat(body.contents().size()).isEqualTo(seatRequests.size());
        boolean hasSameSeats = body.contents().stream()
            .allMatch(seatResponse -> seatRequests.stream()
                .anyMatch(seatRequest -> seatRequest.rowCode() == seatResponse.rowCode()
                    && seatRequest.number() == seatResponse.number()));
        assertThat(hasSameSeats).isTrue();
    }
}
