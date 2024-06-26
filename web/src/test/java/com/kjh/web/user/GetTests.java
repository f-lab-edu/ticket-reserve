package com.kjh.web.user;

import com.kjh.web.EmailFixture;
import com.kjh.web.annotation.AutoDomainSource;
import com.kjh.web.annotation.WebTest;
import com.kjh.web.response.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.kjh.web.TestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebTest
@DisplayName("GET /users/my/info")
class GetTests {

    @ParameterizedTest
    @AutoDomainSource
    void 로그인_후_내정보를_조회하면_OK_상태코드를_반환한다(
        EmailFixture email,
        String password,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        signup(client, email, password);
        String accessToken = signin(client, email, password);

        // Act
        ResponseEntity<UserInfo> response = getWithToken(client, accessToken, "/users/my/info", UserInfo.class);

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
        String accessToken = signin(client, email, password);

        // Act
        ResponseEntity<UserInfo> response = getWithToken(client, accessToken, "/users/my/info", UserInfo.class);

        // Assert
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("email", email.value());
    }
}
