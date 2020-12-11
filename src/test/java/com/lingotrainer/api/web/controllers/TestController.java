package com.lingotrainer.api.web.controllers;

import com.lingotrainer.api.security.jwt.JwtProperties;
import com.lingotrainer.api.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JwtTokenProvider.class, JwtProperties.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class TestController {
}
