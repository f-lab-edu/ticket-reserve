package com.kjh.ticketreserve.user;

import com.kjh.ticketreserve.AccessTokenCarrier;
import com.kjh.ticketreserve.AutoDomainSource;
import com.kjh.ticketreserve.EmailFixture;
import com.kjh.ticketreserve.UserInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetTests {

    @ParameterizedTest
    @AutoDomainSource
    void 로그인_후_내정보를_조회하면_OK_상태코드를_반환한다(
        EmailFixture email,
        String password,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, email, password);
        String accessToken = Objects.requireNonNull(signin(client, email, password, AccessTokenCarrier.class)
            .getBody()).accessToken();

        // Act
        ResponseEntity<UserInfo> response = client.exchange("/user/my/info", HttpMethod.GET,
            new HttpEntity<>(getAuthorizationHeader(accessToken)), UserInfo.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 로그인_후_내정보를_조회하면_사용자_이메일을_반환한다(
        EmailFixture email,
        String password,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, email, password);
        String accessToken = Objects.requireNonNull(signin(client, email, password, AccessTokenCarrier.class)
            .getBody()).accessToken();

        // Act
        ResponseEntity<UserInfo> response = client.exchange("/user/my/info", HttpMethod.GET,
            new HttpEntity<>(getAuthorizationHeader(accessToken)), UserInfo.class);

        // Assert
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("email", email.value());
    }

    private static HttpHeaders getAuthorizationHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private static void signup(TestRestTemplate client, EmailFixture email, String password) {
        Map<String, String> request = new HashMap<>();
        request.put("email", email.value());
        request.put("password", password);
        client.postForEntity("/signup", request, Void.class);
    }

    private static <T> ResponseEntity<T> signin(
        TestRestTemplate client,
        EmailFixture email,
        String password,
        Class<T> responseType
    ) {
        Map<String, String> signRequest = new HashMap<>();
        signRequest.put("email", email.value());
        signRequest.put("password", password);
        return client.postForEntity("/signin", signRequest, responseType);
    }
}
