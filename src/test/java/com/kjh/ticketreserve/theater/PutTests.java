package com.kjh.ticketreserve.theater;

import com.kjh.ticketreserve.*;
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
@DisplayName("PUT /admin/theaters")
public class PutTests {

    @ParameterizedTest
    @AutoDomainSource
    void 영화관_아이디를_사용해_영화관_정보를_수정하면_수정된_정보를_반환한다(
        Credentials credentials,
        TheaterRequest createTheaterRequest,
        TheaterRequest updateTheaterRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long id = createTheater(client, accessToken, createTheaterRequest);

        // Act
        ResponseEntity<TheaterResponse> response = putWithToken(client,
            accessToken,
            "/admin/theaters/" + id,
            updateTheaterRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        TheaterResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(id);
        assertThat(body.name()).isEqualTo(updateTheaterRequest.name());
        assertThat(body.address()).isEqualTo(updateTheaterRequest.address());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_영화관_아이디를_사용해_수정하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        long id,
        TheaterRequest theaterRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);

        // Act
        ResponseEntity<TheaterResponse> response = putWithToken(client,
            accessToken,
            "/admin/theaters/" + id,
            theaterRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 좌석_아이디를_사용해_좌석_정보를_수정하면_수정된_정보를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        SeatRequest createSeatRequest,
        SeatRequest updateSeatRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long seatId = createSeat(client, accessToken, theaterId, createSeatRequest);

        // Act
        ResponseEntity<SeatResponse> response = putWithToken(client,
            accessToken,
            "/admin/theaters/" + theaterId + "/seats/" + seatId,
            updateSeatRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        SeatResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(seatId);
        assertThat(body.rowCode()).isEqualTo(updateSeatRequest.rowCode());
        assertThat(body.number()).isEqualTo(updateSeatRequest.number());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_좌석_아이디를_사용해_수정하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        long seatId,
        SeatRequest seatRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = createTheater(client, accessToken, theaterRequest);

        // Act
        ResponseEntity<SeatResponse> response = putWithToken(client,
            accessToken,
            "/admin/theaters/" + theaterId + "/seats/" + seatId,
            seatRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_영화관_아이디를_사용해_수정하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        long notExistingTheaterId,
        SeatRequest seatRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long seatId = createSeat(client, accessToken, theaterId, seatRequest);

        // Act
        ResponseEntity<SeatResponse> response = putWithToken(client,
            accessToken,
            "/admin/theaters/" + notExistingTheaterId + "/seats/" + seatId,
            seatRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
