package com.lingotrainer.api.config;

import com.lingotrainer.api.security.FilterChainExceptionHandler;
import com.lingotrainer.api.security.jwt.JwtSecurityConfigurer;
import com.lingotrainer.api.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtTokenProvider jwtTokenProvider;

    private FilterChainExceptionHandler filterChainExceptionHandler;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          FilterChainExceptionHandler filterChainExceptionHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.filterChainExceptionHandler = filterChainExceptionHandler;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Boilerplate passive security protocol.
     * Use annotations to secure a specified resource like:
     *  - @Secured("SOME_ROLE")
     *  - @Authenticated
     *
     *
     * @param http Spring HttpSecurity class
     * @throws Exception Any exception that might occur
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class)
                .apply(new JwtSecurityConfigurer(jwtTokenProvider));
    }


}

