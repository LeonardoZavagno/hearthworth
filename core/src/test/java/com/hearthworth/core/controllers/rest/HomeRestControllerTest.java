package com.hearthworth.core.controllers.rest;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
@Property(name = "app.version", value = "test-version")
class HomeRestControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void returnsApplicationVersion() {
        Map<String, String> response = client.toBlocking().retrieve(
                HttpRequest.GET("/app-version"),
                Argument.mapOf(String.class, String.class)
        );

        assertEquals("test-version", response.get("app-version"));
    }
}
