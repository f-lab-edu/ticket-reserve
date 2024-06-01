package com.kjh.admin.showtime;

import com.kjh.admin.annotation.AutoDomainSource;
import com.kjh.admin.annotation.WebTest;
import com.kjh.admin.request.Credentials;
import com.kjh.admin.request.MovieRequest;
import com.kjh.admin.request.ShowtimeRequest;
import com.kjh.admin.request.TheaterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static com.kjh.admin.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("DELETE /showtimes")
class DeleteTests {

    @ParameterizedTest
    @AutoDomainSource
    void 상영시간표_아이디를_사용해_상영시간표를_삭제하면_OK_상태코드를_반환한다(
        Credentials credentials,
        MovieRequest movieRequest,
        TheaterRequest theaterRequest,
        LocalDateTime showDatetime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long id = createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showDatetime));

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/showtimes/" + id,
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
        LocalDateTime showDatetime,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long movieId = createMovie(client, accessToken, movieRequest);
        long theaterId = createTheater(client, accessToken, theaterRequest);
        long id = createShowtime(client, accessToken, new ShowtimeRequest(movieId, theaterId, showDatetime));
        deleteWithToken(client, accessToken, "/showtimes/" + id, Void.class);

        // Act
        ResponseEntity<Object> response = deleteWithToken(client,
            accessToken,
            "/showtimes/" + id,
            Object.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
