package com.kjh.ticketreserve.showtime;

import com.kjh.ticketreserve.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static com.kjh.ticketreserve.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("PUT /admin/showtimes")
public class PutTests {

    @ParameterizedTest
    @AutoDomainSource
    void 상영시간표_아이디를_사용해_상영시간표_정보를_수정하면_수정된_정보를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        LocalDateTime createShowDatetime,
        LocalDateTime updateShowDatetime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long id = createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, createShowDatetime));

        // Act
        ShowtimeUpdateRequest showtimeUpdateRequest = new ShowtimeUpdateRequest(updateShowDatetime);
        ResponseEntity<ShowtimeResponse> response = putWithToken(client,
            accessToken,
            "/admin/showtimes/" + id,
            showtimeUpdateRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        ShowtimeResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(id);
        assertThat(body.movie().id()).isEqualTo(movieId);
        assertThat(body.theater().id()).isEqualTo(theaterId);
        assertThat(body.showDatetime()).isEqualTo(showtimeUpdateRequest.showDatetime());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 존재하지_않는_상영시간표_아이디를_사용해_수정하면_Not_Found_상태코드를_반환한다(
        Credentials credentials,
        long id,
        ShowtimeUpdateRequest showtimeUpdateRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);

        // Act
        ResponseEntity<ShowtimeResponse> response = putWithToken(client,
            accessToken,
            "/admin/showtimes/" + id,
            showtimeUpdateRequest,
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
