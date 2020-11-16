package com.lingotrainer.api.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

	private String secretKey = "gekkelingo";

	// default: 3600000
	// valid for 1 hour
	private long validityInMs = 36000000;
}
