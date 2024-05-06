package com.kjh.ticketreserve.movie;

import com.kjh.ticketreserve.AutoDomainSource;
import com.kjh.ticketreserve.Credentials;
import com.kjh.ticketreserve.MovieRequest;
import com.kjh.ticketreserve.MovieResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("PUT /admin/movie")
public class PutTests {

    @ParameterizedTest
    @AutoDomainSource
    void 영화_아이디를_사용해_영화_정보를_수정하면_수정된_정보를_반환한다(
        Credentials credentials,
        MovieRequest createMovieRequest,
        MovieRequest updateMovieRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long id = createMovie(client, accessToken, createMovieRequest);

        // Act
        ResponseEntity<MovieResponse> response = putWithToken(client,
            accessToken,
            "/admin/movie/" + id,
            updateMovieRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        MovieResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(id);
        assertThat(body.title()).isEqualTo(updateMovieRequest.title());
        assertThat(body.startDate()).isEqualToIgnoringNanos(updateMovieRequest.startDate());
        assertThat(body.endDate()).isEqualToIgnoringNanos(updateMovieRequest.endDate());
        assertThat(body.price()).isEqualTo(updateMovieRequest.price());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_영화_아이디를_사용해_수정하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        long id,
        MovieRequest updateMovieRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);

        // Act
        ResponseEntity<MovieResponse> response = putWithToken(client,
            accessToken,
            "/admin/movie/" + id,
            updateMovieRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
