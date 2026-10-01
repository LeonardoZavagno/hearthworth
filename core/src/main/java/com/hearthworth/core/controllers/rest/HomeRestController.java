package com.hearthworth.core.controllers.rest;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeRestController {

    @Value("${app.version}")
    private String appVersion;

    @Get("/app-version")
    public Map<String, String> getStatus() {
        Map<String, String> map = new HashMap<>();
        map.put("app-version", appVersion);
        return map;
    }
}
