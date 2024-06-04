package com.kjh.admin.theater;

import com.kjh.admin.annotation.AutoDomainSource;
import com.kjh.admin.annotation.WebTest;
import com.kjh.admin.request.Credentials;
import com.kjh.admin.request.SeatRequest;
import com.kjh.admin.request.TheaterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.kjh.admin.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("DELETE /theaters")
class DeleteTests {

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
            "/theaters/" + id,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_영화관_아이디를_사용해_영화관을_삭제하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long id = createTheater(client, accessToken, theaterRequest);
        deleteWithToken(client, accessToken, "/theaters/" + id, Void.class);

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/theaters/" + id,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 좌석_아이디를_사용해_좌석을_삭제하면_OK_상태코드를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        SeatRequest seatRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long seatId = createSeat(client, accessToken, theaterId, seatRequest);

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/theaters/" + theaterId + "/seats/" + seatId,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_좌석_아이디를_사용해_좌석을_삭제하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        SeatRequest seatRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long seatId = createSeat(client, accessToken, theaterId, seatRequest);
        deleteWithToken(client, accessToken, "/theaters/" + theaterId + "/seats/" + seatId, Void.class);

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/theaters/" + theaterId + "/seats/" + seatId,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_영화관_아이디를_사용해_좌석을_삭제하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        SeatRequest seatRequest,
        long notExistingTheaterId,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long seatId = createSeat(client, accessToken, theaterId, seatRequest);

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/theaters/" + notExistingTheaterId + "/seats/" + seatId,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
