package com.kjh.ticketreserve.movie;

import com.kjh.ticketreserve.AutoDomainSource;
import com.kjh.ticketreserve.Credentials;
import com.kjh.ticketreserve.MovieRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("POST /admin/movie")
public class PostTests {

    @ParameterizedTest
    @AutoDomainSource
    void 영화를_등록하면_아이디를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        @Autowired TestRestTemplate client
    ) {
        signup(client, credentials);
        String accessToken = signin(client, credentials);

        ResponseEntity<Map<String, Object>> response = postWithToken(
            client,
            accessToken,
            "/admin/movie",
            movieRequest,
            new ParameterizedTypeReference<>() {
            });

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).hasFieldOrProperty("id");
    }
}
