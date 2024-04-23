package com.kjh.ticketreserve.signin;

import com.kjh.ticketreserve.AutoDomainSource;
import com.kjh.ticketreserve.EmailFixture;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostTests {

    @ParameterizedTest
    @AutoDomainSource
    void 잘못된_비밀번호를_사용해_로그인_하면_Bad_Request_상태코드를_반환한다(
        EmailFixture email,
        String password,
        String wrongPassword,
        @Autowired TestRestTemplate client
    ) {
        signup(client, email, password);
        ResponseEntity<Void> response = signin(client, email, wrongPassword);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 잘못된_이메일을_사용해_로그인_하면_Bad_Request_상태코드를_반환한다(
        EmailFixture email,
        String password,
        EmailFixture wrongEmail,
        @Autowired TestRestTemplate client
    ) {
        signup(client, email, password);
        ResponseEntity<Void> response = signin(client, wrongEmail, password);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    private static void signup(TestRestTemplate client, EmailFixture email, String password) {
        Map<String, String> request = new HashMap<>();
        request.put("email", email.value());
        request.put("password", password);
        client.postForEntity("/signup", request, Void.class);
    }

    private static ResponseEntity<Void> signin(
        TestRestTemplate client,
        EmailFixture email,
        String password
    ) {
        Map<String, String> signRequest = new HashMap<>();
        signRequest.put("email", email.value());
        signRequest.put("password", password);
        return client.postForEntity("/signin", signRequest, Void.class);
    }
}
