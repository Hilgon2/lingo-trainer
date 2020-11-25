package com.lingotrainer.api.infrastructure.web.controllers;

import com.lingotrainer.api.application.authentication.AuthenticationService;
import com.lingotrainer.api.infrastructure.web.request.AuthenticationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Map<Object, Object>> login(@RequestBody AuthenticationRequest data) {
        return ok(this.authenticationService.login(data));
    }
}
