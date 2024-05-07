package com.kjh.ticketreserve.movie;

import com.kjh.ticketreserve.AutoDomainSource;
import com.kjh.ticketreserve.Credentials;
import com.kjh.ticketreserve.MovieRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("DELETE /admin/movies")
public class DeleteTests {

    @ParameterizedTest
    @AutoDomainSource
    void 영화_아이디를_사용해_영화를_삭제하면_OK_상태코드를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long id = createMovie(client, accessToken, movieRequest);

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/admin/movies/" + id,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_영화_아이디를_사용해_삭제하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long id = createMovie(client, accessToken, movieRequest);
        deleteWithToken(client, accessToken, "/admin/movies/" + id, Void.class);

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/admin/movies/" + id,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
