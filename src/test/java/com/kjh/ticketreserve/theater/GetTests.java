package com.kjh.ticketreserve.theater;

import com.kjh.ticketreserve.AutoDomainSource;
import com.kjh.ticketreserve.Credentials;
import com.kjh.ticketreserve.TheaterRequest;
import com.kjh.ticketreserve.TheaterResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("GET /theaters")
public class GetTests {

    @ParameterizedTest
    @AutoDomainSource
    void 영화관_아이디를_사용해_조회하면_영화관_정보를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long id = createTheater(client, accessToken, theaterRequest);

        // Act
        ResponseEntity<TheaterResponse> response = getWithToken(client,
            accessToken,
            "/theaters/" + id,
            TheaterResponse.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        TheaterResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(id);
        assertThat(body.name()).isEqualTo(theaterRequest.name());
        assertThat(body.address()).isEqualTo(theaterRequest.address());
    }
}
