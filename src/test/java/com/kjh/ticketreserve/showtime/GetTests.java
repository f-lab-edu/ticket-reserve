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
@DisplayName("GET /showtimes")
public class GetTests {

    @ParameterizedTest
    @AutoDomainSource
    void 상영시간표_아이디를_사용해_조회하면_상영시간표_정보를_반환한다(
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
        ResponseEntity<ShowtimeResponse> response = getWithToken(client,
            accessToken,
            "/showtimes/" + id,
            ShowtimeResponse.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        ShowtimeResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(id);
        assertThat(body.movie().id()).isEqualTo(movieId);
        assertThat(body.theater().id()).isEqualTo(theaterId);
        assertThat(body.showtime()).isEqualToIgnoringNanos(showtime);
    }
}
