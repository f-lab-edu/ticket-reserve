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
import org.springframework.http.ResponseEntity;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("GET /admin/movies")
public class GetTests {

    @ParameterizedTest
    @AutoDomainSource
    void 영화_아이디를_사용해_조회하면_영화_정보를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        @Autowired TestRestTemplate client
    ) {
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long id = createMovie(client, accessToken, movieRequest);

        ResponseEntity<MovieResponse> response = getWithToken(
            client,
            accessToken,
            "/admin/movies/" + id,
            MovieResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        MovieResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(id);
        assertThat(body.title()).isEqualTo(movieRequest.title());
        assertThat(body.startDate()).isEqualToIgnoringNanos(movieRequest.startDate());
        assertThat(body.endDate()).isEqualToIgnoringNanos(movieRequest.endDate());
        assertThat(body.price()).isEqualTo(movieRequest.price());
    }
}
