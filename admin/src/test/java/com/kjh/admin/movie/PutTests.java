package com.kjh.admin.movie;

import com.kjh.admin.annotation.AutoDomainSource;
import com.kjh.admin.annotation.WebTest;
import com.kjh.admin.request.Credentials;
import com.kjh.admin.request.MovieRequest;
import com.kjh.admin.response.MovieResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import static com.kjh.admin.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("PUT /movies")
class PutTests {

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
            "/movies/" + id,
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
            "/movies/" + id,
            updateMovieRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
