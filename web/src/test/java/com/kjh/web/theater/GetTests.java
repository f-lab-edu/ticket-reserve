package com.kjh.web.theater;

import com.kjh.core.response.ArrayResponse;
import com.kjh.core.response.PageResponse;
import com.kjh.web.SeatRequest;
import com.kjh.web.TestDataService;
import com.kjh.web.TheaterRequest;
import com.kjh.web.annotation.AutoDomainSource;
import com.kjh.web.annotation.WebTest;
import com.kjh.web.request.Credentials;
import com.kjh.web.response.SeatResponse;
import com.kjh.web.response.TheaterResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.Comparator;
import java.util.List;

import static com.kjh.web.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("GET /theaters")
class GetTests {

    @Autowired
    private TestDataService testDataService;

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
        long id = testDataService.createTheater(theaterRequest).getId();

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

    @ParameterizedTest
    @AutoDomainSource
    void 영화관을_검색하면_영화관_리스트를_반환한다(
        Credentials credentials,
        List<TheaterRequest> theaterRequests,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        for (TheaterRequest theaterRequest : theaterRequests) {
            testDataService.createTheater(theaterRequest);
        }

        // Act
        ResponseEntity<PageResponse<TheaterResponse>> response = getWithToken(client,
            accessToken,
            "/theaters?pageNumber=0&pageSize=" + theaterRequests.size(),
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content().size()).isEqualTo(theaterRequests.size());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 영화관_이름을_사용해_검색하면_조건에_맞는_영화관_리스트를_반환한다(
        Credentials credentials,
        List<TheaterRequest> theaterRequests,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        for (TheaterRequest theaterRequest : theaterRequests) {
            testDataService.createTheater(theaterRequest);
        }

        // Act
        TheaterRequest theaterToFind = theaterRequests.get(1);
        ResponseEntity<PageResponse<TheaterResponse>> response = getWithToken(client,
            accessToken,
            "/theaters?pageNumber=0&pageSize=" + theaterRequests.size() + "&name=" + theaterToFind.name(),
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content().size()).isEqualTo(1);
        TheaterResponse found = response.getBody().content().get(0);
        assertThat(found.name()).isEqualTo(theaterToFind.name());
        assertThat(found.address()).isEqualTo(theaterToFind.address());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 영화관_주소를_사용해_검색하면_조건에_맞는_영화관_리스트를_반환한다(
        Credentials credentials,
        List<TheaterRequest> theaterRequests,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        for (TheaterRequest theaterRequest : theaterRequests) {
            testDataService.createTheater(theaterRequest);
        }

        // Act
        TheaterRequest theaterToFind = theaterRequests.get(1);
        ResponseEntity<PageResponse<TheaterResponse>> response = getWithToken(client,
            accessToken,
            "/theaters?pageNumber=0&pageSize=" + theaterRequests.size() + "&address=" + theaterToFind.address(),
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content().size()).isEqualTo(1);
        TheaterResponse found = response.getBody().content().get(0);
        assertThat(found.name()).isEqualTo(theaterToFind.name());
        assertThat(found.address()).isEqualTo(theaterToFind.address());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 좌석_아이디를_사용해_조회하면_좌석_정보를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        SeatRequest seatRequest,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = testDataService.createTheater(theaterRequest).getId();
        long seatId = testDataService.createSeat(theaterId, seatRequest).getId();

        // Act
        ResponseEntity<SeatResponse> response = getWithToken(client,
            accessToken,
            "/theaters/" + theaterId + "/seats/" + seatId,
            SeatResponse.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        SeatResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(seatId);
        assertThat(body.rowCode()).isEqualTo(seatRequest.rowCode());
        assertThat(body.number()).isEqualTo(seatRequest.number());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 좌석을_검색하면_정렬된_좌석_리스트를_반환한다(
        Credentials credentials,
        TheaterRequest theaterRequest,
        List<SeatRequest> seatRequests,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, credentials);
        String accessToken = signin(client, credentials);
        long theaterId = testDataService.createTheater(theaterRequest).getId();
        for (SeatRequest seatRequest : seatRequests) {
            testDataService.createSeat(theaterId, seatRequest);
        }

        // Act
        ResponseEntity<ArrayResponse<SeatResponse>> response = getWithToken(client,
            accessToken,
            "/theaters/" + theaterId + "/seats",
            new ParameterizedTypeReference<>() {
            });

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().contents().size()).isEqualTo(seatRequests.size());
        assertThat(response.getBody().contents()).isSortedAccordingTo(
            Comparator.comparing(SeatResponse::rowCode).thenComparing(SeatResponse::number));
    }
}
