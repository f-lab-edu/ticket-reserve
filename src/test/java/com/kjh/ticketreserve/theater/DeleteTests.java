package com.kjh.ticketreserve.theater;

import com.kjh.ticketreserve.AutoDomainSource;
import com.kjh.ticketreserve.Credentials;
import com.kjh.ticketreserve.TheaterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("DELETE /admin/theaters")
public class DeleteTests {

    @ParameterizedTest
    @AutoDomainSource
    void 영화관_아이디를_사용해_영화관을_삭제하면_OK_상태코드를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long id = createTheater(client, accessToken, theaterRequest);

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/admin/theaters/" + id,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_영화관_아이디를_사용해_삭제하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long id = createTheater(client, accessToken, theaterRequest);
        deleteWithToken(client, accessToken, "/admin/theaters/" + id, Void.class);

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/admin/theaters/" + id,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
