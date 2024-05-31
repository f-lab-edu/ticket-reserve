package com.kjh.ticketreserve.movie;

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
import java.util.List;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("GET /admin/movies")
public class GetTests {

    @ParameterizedTest
    @AutoDomainSource
    void 영화_아이디를_사용해_조회하면_영화_정보를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long id = createMovie(client, accessToken, movieRequest);

        // Act
        ResponseEntity<MovieResponse> response = getWithToken(
            client,
            accessToken,
            "/admin/movies/" + id,
            MovieResponse.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        MovieResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(id);
        assertThat(body.title()).isEqualTo(movieRequest.title());
        assertThat(body.startDate()).isEqualToIgnoringNanos(movieRequest.startDate());
        assertThat(body.endDate()).isEqualToIgnoringNanos(movieRequest.endDate());
        assertThat(body.price()).isEqualTo(movieRequest.price());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 영화를_검색하면_영화_리스트를_반환한다(
        Credentials credentials,
        List<MovieRequest> movieRequests,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        for (MovieRequest movieRequest : movieRequests) {
            createMovie(client, accessToken, movieRequest);
        }

        // Act
        ResponseEntity<PageResponse<MovieResponse>> response = getWithToken(client,
            accessToken,
            "/admin/movies?pageNumber=0&pageSize=" + movieRequests.size(),
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content().size()).isEqualTo(movieRequests.size());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 영화_제목을_사용해_검색하면_조건에_맞는_영화_리스트를_반환한다(
        Credentials credentials,
        List<MovieRequest> movieRequests,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        for (MovieRequest movieRequest : movieRequests) {
            createMovie(client, accessToken, movieRequest);
        }

        // Act
        MovieRequest movieToFind = movieRequests.get(0);
        ResponseEntity<PageResponse<MovieResponse>> response = getWithToken(client,
            accessToken,
            "/admin/movies?pageNumber=0&pageSize=" + movieRequests.size() + "&title=" + movieToFind.title(),
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content().size()).isEqualTo(1);
        MovieResponse found = response.getBody().content().get(0);
        assertThat(found.title()).isEqualTo(movieToFind.title());
        assertThat(found.startDate()).isEqualToIgnoringNanos(found.startDate());
        assertThat(found.endDate()).isEqualToIgnoringNanos(found.endDate());
        assertThat(found.price()).isEqualTo(movieToFind.price());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 영화_상영일을_사용해_검색하면_조건에_맞는_영화_리스트를_반환한다(
        Credentials credentials,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        List<MovieRequest> movieRequests = List.of(
            new MovieRequest("movie1",
                LocalDateTime.of(2024, 5, 1, 0, 0),
                LocalDateTime.of(2024, 5, 10, 23, 59),
                10000),
            new MovieRequest("movie2",
                LocalDateTime.of(2024, 5, 2, 0, 0),
                LocalDateTime.of(2024, 5, 10, 23, 59),
                10000),
            new MovieRequest("movie3",
                LocalDateTime.of(2024, 4, 1, 0, 0),
                LocalDateTime.of(2024, 4, 30, 23, 59),
                10000)
        );
        for (MovieRequest movieRequest : movieRequests) {
            createMovie(client, accessToken, movieRequest);
        }

        // Act
        MovieRequest movieToFind = movieRequests.get(0);
        ResponseEntity<PageResponse<MovieResponse>> response = getWithToken(client,
            accessToken,
            "/admin/movies?pageNumber=0&pageSize=" + movieRequests.size() + "&screeningDate=" + movieToFind.startDate(),
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content().size()).isEqualTo(1);
        MovieResponse found = response.getBody().content().get(0);
        assertThat(found.title()).isEqualTo(movieToFind.title());
        assertThat(found.startDate()).isEqualToIgnoringNanos(found.startDate());
        assertThat(found.endDate()).isEqualToIgnoringNanos(found.endDate());
        assertThat(found.price()).isEqualTo(movieToFind.price());
    }
}
