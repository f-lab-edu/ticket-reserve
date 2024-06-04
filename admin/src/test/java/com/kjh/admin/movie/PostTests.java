package com.kjh.admin.movie;

import com.kjh.admin.annotation.AutoDomainSource;
import com.kjh.admin.annotation.WebTest;
import com.kjh.admin.request.Credentials;
import com.kjh.admin.request.MovieRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static com.kjh.admin.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("POST /movies")
class PostTests {

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
            "/movies",
            movieRequest,
            new ParameterizedTypeReference<>() {
            });

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).hasFieldOrProperty("id");
    }
}
