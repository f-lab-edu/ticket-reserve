package com.kjh.ticketreserve;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class TestLanguage {

    public static void signup(TestRestTemplate client, EmailFixture email, String password) {
        signup(client, email.value(), password);
    }

    public static void signup(TestRestTemplate client, Credentials credentials) {
        signup(client, credentials.email(), credentials.password());
    }

    public static void signup(TestRestTemplate client, String email, String password) {
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);
        client.postForEntity("/signup", request, Void.class);
    }

    public static String signin(
        TestRestTemplate client,
        EmailFixture email,
        String password
    ) {
        return signin(client, email.value(), password);
    }
    public static String signin(
        TestRestTemplate client,
        Credentials credentials
    ) {
        return signin(client, credentials.email(), credentials.password());
    }

    @SuppressWarnings("DataFlowIssue")
    private static String signin(TestRestTemplate client, String email, String password) {
        Map<String, String> signRequest = new HashMap<>();
        signRequest.put("email", email);
        signRequest.put("password", password);
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/signin", signRequest, AccessTokenCarrier.class);
        return response.getBody().accessToken();
    }

    public static <T> ResponseEntity<T> getWithToken(TestRestTemplate client,
                                                     String accessToken,
                                                     String path,
                                                     Class<T> type) {
        return client.exchange(path, HttpMethod.GET, new HttpEntity<>(getHttpHeaders(accessToken)), type);
    }

    public static <T> ResponseEntity<T> getWithToken(TestRestTemplate client,
                                                     String accessToken,
                                                     String path,
                                                     ParameterizedTypeReference<T> type) {
        return client.exchange(path, HttpMethod.GET, new HttpEntity<>(getHttpHeaders(accessToken)), type);
    }

    public static <Request, Response> ResponseEntity<Response> postWithToken(
        TestRestTemplate client,
        String accessToken,
        String path,
        Request request,
        ParameterizedTypeReference<Response> responseType) {

        return executeWithToken(client, accessToken, path, HttpMethod.POST, request, responseType);
    }

    public static <Request, Response> ResponseEntity<Response> putWithToken(
        TestRestTemplate client,
        String accessToken,
        String path,
        Request request,
        ParameterizedTypeReference<Response> responseType) {

        return executeWithToken(client, accessToken, path, HttpMethod.PUT, request, responseType);
    }

    public static <T> ResponseEntity<T> deleteWithToken(TestRestTemplate client,
                                                        String accessToken,
                                                        String path,
                                                        Class<T> type) {
        return client.exchange(path, HttpMethod.DELETE, new HttpEntity<>(getHttpHeaders(accessToken)), type);
    }

    private static HttpHeaders getHttpHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private static <Request, Response> ResponseEntity<Response> executeWithToken(
        TestRestTemplate client,
        String accessToken,
        String path,
        HttpMethod method,
        Request request,
        ParameterizedTypeReference<Response> responseType
    ) {
        return client.exchange(path,
            method,
            new HttpEntity<>(request, getHttpHeaders(accessToken)),
            responseType);
    }

    @SuppressWarnings("DataFlowIssue")
    public static long createMovie(
        TestRestTemplate client,
        String accessToken,
        MovieRequest movieRequest) {

        ResponseEntity<MovieResponse> response = postWithToken(
            client,
            accessToken,
            "/admin/movies",
            movieRequest,
            new ParameterizedTypeReference<>() {
            });

        return response.getBody().id();
    }

    @SuppressWarnings("DataFlowIssue")
    public static long createTheater(
        TestRestTemplate client,
        String accessToken,
        TheaterRequest theaterRequest) {

        ResponseEntity<TheaterResponse> response = postWithToken(client,
            accessToken,
            "/admin/theaters",
            theaterRequest,
            new ParameterizedTypeReference<>() {
            });

        return response.getBody().id();
    }
}
