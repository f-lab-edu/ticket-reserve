package com.kjh.ticketreserve.signup;

import com.kjh.ticketreserve.*;
import com.kjh.ticketreserve.annotation.AutoDomainSource;
import com.kjh.ticketreserve.annotation.WebTest;
import com.kjh.ticketreserve.exception.BadRequestException;
import com.kjh.ticketreserve.jpa.UserRepository;
import com.kjh.ticketreserve.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("POST /signup")
public class PostTests {

    @ParameterizedTest
    @AutoDomainSource
    void 올바른_자격증명을_사용해_회원가입_하면_OK_상태코드를_반환한다(
        String localPart,
        String password,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = localPart + "@test.com";
        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);

        // Act
        ResponseEntity<Void> response = client.postForEntity("/signup", request, Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 올바른_자격증명을_사용해_회원가입_하면_로그인에_성공한다(
        String localPart,
        String password,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = localPart + "@test.com";
        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);

        // Act
        client.postForEntity("/signup", request, Void.class);

        // Assert
        ResponseEntity<Void> response = client.postForEntity("/signin", request, Void.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @ParameterizedTest
    @AutoDomainSource
    void 이미_존재하는_이메일을_사용해_회원가입_하면_Bad_Request_상태코드와_메시지를_반환한다(
        String localPart,
        String password,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = localPart + "@test.com";
        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);
        client.postForEntity("/signup", request, Void.class);

        // Act
        ResponseEntity<ErrorResponse> response = client.postForEntity("/signup", request, ErrorResponse.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue(
            "message", BadRequestException.DUPLICATED_EMAIL.get().getMessage());
    }

    @ParameterizedTest
    @AutoDomainSource
    void 올바른_자격증명을_사용해_회원가입_하면_비밀번호가_암호화된다(
        EmailFixture email,
        String password,
        @Autowired TestRestTemplate client,
        @Autowired UserRepository repository
    ) {
        // Arrange
        Credentials credentials = new Credentials(email.value(), password);

        // Act
        client.postForEntity("/signup", credentials, Void.class);

        // Assert
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        User user = repository.findByEmail(email.value()).get();
        assertThat(user.getPasswordHash()).isNotEqualTo(password);
    }
}
