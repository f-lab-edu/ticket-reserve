package com.kjh.admin.signin;

import com.kjh.admin.EmailFixture;
import com.kjh.admin.annotation.AutoDomainSource;
import com.kjh.admin.annotation.WebTest;
import com.kjh.admin.response.AccessTokenCarrier;
import com.kjh.core.exception.BadRequestException;
import com.kjh.core.response.ErrorResponse;
import com.kjh.core.security.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("POST /signin")
class PostTests {

    @ParameterizedTest
    @AutoDomainSource
    void 잘못된_비밀번호를_사용해_로그인_하면_Bad_Request_상태코드와_메시지를_반환한다(
        EmailFixture email,
        String password,
        String wrongPassword,
        @Autowired TestRestTemplate client
    ) {
        signup(client, email, password);
        ResponseEntity<ErrorResponse> response = signin(client, email, wrongPassword, ErrorResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue(
            "message", BadRequestException.BAD_CREDENTIALS.get().getMessage());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 잘못된_이메일을_사용해_로그인_하면_Bad_Request_상태코드와_메시지를_반환한다(
        EmailFixture email,
        String password,
        EmailFixture wrongEmail,
        @Autowired TestRestTemplate client
    ) {
        signup(client, email, password);
        ResponseEntity<ErrorResponse> response = signin(client, wrongEmail, password, ErrorResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue(
            "message", BadRequestException.BAD_CREDENTIALS.get().getMessage());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 올바른_이메일과_비밀번호를_사용해_로그인_하면_OK_상태코드를_반환한다(
        EmailFixture email,
        String password,
        @Autowired TestRestTemplate client
    ) {
        signup(client, email, password);
        ResponseEntity<AccessTokenCarrier> response = signin(client, email, password, AccessTokenCarrier.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 올바른_이메일과_비밀번호를_사용해_로그인_하면_유효한_토큰을_반환한다(
        EmailFixture email,
        String password,
        @Autowired TestRestTemplate client,
        @Autowired JwtProvider jwtProvider
    ) {
        signup(client, email, password);
        ResponseEntity<AccessTokenCarrier> response = signin(client, email, password, AccessTokenCarrier.class);
        assertThat(jwtProvider.validateToken(Objects.requireNonNull(response.getBody()).accessToken())).isTrue();
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
