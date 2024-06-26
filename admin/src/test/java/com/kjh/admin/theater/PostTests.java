package com.kjh.admin.theater;

import com.kjh.admin.annotation.AutoDomainSource;
import com.kjh.admin.annotation.WebTest;
import com.kjh.admin.request.Credentials;
import com.kjh.admin.request.SeatRequest;
import com.kjh.admin.request.TheaterRequest;
import com.kjh.admin.response.SeatResponse;
import com.kjh.admin.response.TheaterResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import static com.kjh.admin.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("POST /theaters")
class PostTests {

    @ParameterizedTest
    @AutoDomainSource
    void 영화관을_등록하면_아이디를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);

        // Act
        ResponseEntity<TheaterResponse> response = postWithToken(client,
            accessToken,
            "/theaters",
            theaterRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).hasFieldOrProperty("id");
    }

    @ParameterizedTest
    @AutoDomainSource
    void 좌석을_등록하면_아이디를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        SeatRequest seatRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = createTheater(client, accessToken, theaterRequest);

        // Act
        ResponseEntity<SeatResponse> response = postWithToken(client,
            accessToken,
            "/theaters/" + theaterId + "/seats",
            seatRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).hasFieldOrProperty("id");
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_영화관_아이디를_사용해_등록하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        long theaterId,
        SeatRequest seatRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);

        // Act
        ResponseEntity<SeatResponse> response = postWithToken(client,
            accessToken,
            "/theaters/" + theaterId + "/seats",
            seatRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 이미_존재하는_영화관_행_열을_사용해_등록하면_Bad_Request_상태코드를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        SeatRequest seatRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        createSeat(client, accessToken, theaterId, seatRequest);

        // Act
        ResponseEntity<SeatResponse> response = postWithToken(client,
            accessToken,
            "/theaters/" + theaterId + "/seats",
            seatRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
