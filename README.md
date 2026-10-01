# Hearth Worth

[![Build and test](https://img.shields.io/github/actions/workflow/status/LeonardoZavagno/hearthworth/build-and-test.yml?label=Build%20and%20test)](https://github.com/LeonardoZavagno/hearthworth/actions/workflows/build-and-test.yml)
[![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2FLeonardoZavagno%2Fhearthworth%2Fmain%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D%27project%27%5D%2F*%5Blocal-name()%3D%27properties%27%5D%2F*%5Blocal-name()%3D%27jdk.version%27%5D%2Ftext()&label=Java&logo=openjdk&color=007396)](https://www.oracle.com/java/)
[![Micronaut](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2FLeonardoZavagno%2Fhearthworth%2Fmain%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D%27project%27%5D%2F*%5Blocal-name()%3D%27parent%27%5D%2F*%5Blocal-name()%3D%27version%27%5D%2Ftext()&label=Micronaut&color=1F7A8C)](https://micronaut.io/)
[![License](https://img.shields.io/github/license/LeonardoZavagno/hearthworth)](LICENSE)

Demo project for family finance data management.

This project is designed to help families track cash flow and net worth in a simple and secure way.

Hearth Worth is a Micronaut-based application with a SQLite database, a ReactJS frontend, JWT-based stateless authentication, and a REST API for managing household finances.

## Authentication

Micronaut Security provides stateless bearer-token authentication. `POST /login` accepts
`{"username":"...","password":"..."}` and returns an `access_token`; send it as
`Authorization: Bearer <access_token>` to access `/api/**`. `GET /api/me` returns the
authenticated username and roles. `/app-version` remains public.

The current setup uses one configured account, like the SpringSport example. In the
development environment it uses the local `leonardo` account; the password is BCrypt-hashed
in memory. Production reads `APP_USER_NAME`, `APP_USER_PASSWORD`, and `JWT_SECRET` from
the environment. JWTs use HS512; `JWT_SECRET` must be Base64-encoded and contain at least
64 random bytes (for example, generate one with `openssl rand -base64 64`). Do not reuse
the same signing key across environments or projects. Tokens expire after 3600
seconds by default in both environments; production can override this with
`JWT_EXPIRATION_SECONDS`.