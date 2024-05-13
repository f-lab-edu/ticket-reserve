package com.kjh.ticketreserve.showtime;

import com.kjh.ticketreserve.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("DELETE /admin/showtimes")
public class DeleteTests {

    @ParameterizedTest
    @AutoDomainSource
    void 상영시간표_아이디를_사용해_상영시간표를_삭제하면_OK_상태코드를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        LocalDateTime showtime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long id = createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showtime));

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/admin/showtimes/" + id,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_상영시간표_아이디를_사용해_삭제하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        LocalDateTime showtime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long id = createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showtime));
        deleteWithToken(client, accessToken, "/admin/showtimes/" + id, Void.class);

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/admin/showtimes/" + id,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
