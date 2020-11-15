package com.lingotrainer.api.controller;

import com.lingotrainer.api.annotation.Public;
import com.lingotrainer.api.model.AuthenticationRequest;
import com.lingotrainer.api.security.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    private JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/login")
    @Public
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest data) throws Exception {
        System.out.println("\n" +  data);
        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));

            // Create the token and maps all the roles to the Spring RoleAuthority so Spring can handle the roles
            String token = jwtTokenProvider.createToken(username);

            Map<Object, Object> model = new HashMap<>();
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            throw new BadCredentialsException("Invalid username/password");
        }
    }
}
