package com.hearthworth.core.security;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import jakarta.inject.Singleton;
import org.mindrot.jbcrypt.BCrypt;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Singleton
public class ConfiguredUserAuthenticationProvider<B> implements HttpRequestAuthenticationProvider<B> {

    private final String username;
    private final String passwordHash;
    private final List<String> roles;

    public ConfiguredUserAuthenticationProvider(
            @Value("${app.security.user.name}") String username,
            @Value("${app.security.user.password}") String password,
            @Value("${app.security.user.roles}") String roles) {
        this.username = username;
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        this.roles = Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .toList();
    }

    @Override
    @NonNull
    public AuthenticationResponse authenticate(
            @Nullable HttpRequest<B> requestContext,
            @NonNull AuthenticationRequest<String, String> authenticationRequest) {
        String suppliedPassword = authenticationRequest.getSecret();
        if (username.equals(authenticationRequest.getIdentity())
                && Objects.nonNull(suppliedPassword)
                && BCrypt.checkpw(suppliedPassword, passwordHash)) {
            return AuthenticationResponse.success(username, roles);
        }
        return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
    }
}
