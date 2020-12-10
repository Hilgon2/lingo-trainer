package com.lingotrainer.api.security;

import lombok.extern.slf4j.Slf4j;
import com.lingotrainer.api.security.jwt.InvalidJwtAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * It can happen that an exception occurs outside a controller and/or a service.
 * Due to the way Spring handles errors we need to add a filter that catched those exceptions.
 */
@Component
@Slf4j
public class FilterChainExceptionHandler extends OncePerRequestFilter {

    private HandlerExceptionResolver resolver;

    public FilterChainExceptionHandler(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (InvalidJwtAuthenticationException e) {
            log.error("Spring Security Filter Chain Exception:", e);
            response.setStatus(500);
            resolver.resolveException(request, response, null, e);
        }
    }
}
