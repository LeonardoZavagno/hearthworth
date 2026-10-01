package com.hearthworth.core.controllers.rest;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import java.util.Map;

@Controller("/api")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AuthenticatedUserController {

    @Get("/me")
    public Map<String, Object> getAuthenticatedUser(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "roles", authentication.getRoles()
        );
    }
}
