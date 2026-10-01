package com.hearthworth.core.controllers.rest;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class AuthenticationTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void loginReturnsJwtThatCanAccessProtectedEndpoint() {
        Map<String, Object> loginResponse = client.toBlocking().retrieve(
                HttpRequest.POST("/login", Map.of("username", "test-user", "password", "test-password")),
                Argument.mapOf(String.class, Object.class)
        );
        String accessToken = (String) loginResponse.get("access_token");

        assertTrue(accessToken != null && !accessToken.isBlank());

        Map<String, Object> userResponse = client.toBlocking().retrieve(
                HttpRequest.GET("/api/me").bearerAuth(accessToken),
                Argument.mapOf(String.class, Object.class)
        );

        assertEquals("test-user", userResponse.get("username"));
        assertEquals(List.of("ROLE_USER"), userResponse.get("roles"));
    }

    @Test
    void loginRejectsInvalidCredentials() {
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(
                        HttpRequest.POST("/login", Map.of("username", "test-user", "password", "wrong-password"))
                )
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void protectedEndpointRejectsRequestsWithoutToken() {
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(HttpRequest.GET("/api/me"))
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }
}
