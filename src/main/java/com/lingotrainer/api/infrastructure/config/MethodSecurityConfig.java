package com.lingotrainer.api.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * This configuration class enabled the following annotation:
 *  - The prePostEnabled property enables Spring Security pre/post annotations
 *  - The securedEnabled property determines if the @Secured annotation should be enabled
 *  - The jsr250Enabled property allows us to use the @RoleAllowed annotation
 *
 * This is necessary if we want to use auth annotations to controllers
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
}