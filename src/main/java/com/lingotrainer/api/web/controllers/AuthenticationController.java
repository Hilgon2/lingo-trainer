package com.lingotrainer.api.web.controllers;

import com.lingotrainer.api.web.mapper.LoginFormMapper;
import com.lingotrainer.api.web.response.LoginResponse;
import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.api.web.request.AuthenticationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;
    private LoginFormMapper loginFormMapper;

    public AuthenticationController(AuthenticationService authenticationService, LoginFormMapper loginFormMapper) {
        this.authenticationService = authenticationService;
        this.loginFormMapper = loginFormMapper;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationRequest data) {
        return ok(this.loginFormMapper.convertToResponse(this.authenticationService.login(data)));
    }

    @PostMapping(path = "/logged-in")
    public ResponseEntity<Void>  checkLoggedIn() {
        this.authenticationService.checkLoggedIn();
        return noContent().build();
    }
}
