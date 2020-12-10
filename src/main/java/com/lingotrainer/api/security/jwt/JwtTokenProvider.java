package com.lingotrainer.api.security.jwt;

import com.google.gson.Gson;
import com.lingotrainer.application.user.UserService;
import com.lingotrainer.domain.model.user.User;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private JwtProperties jwtProperties;
    private UserService userService;
    private String secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties, UserService userService) {
        this.jwtProperties = jwtProperties;
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    public String createToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getValidityInMs());
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        claims.put("highscore", user.getHighscore());

        return Jwts.builder()
                .claim("user", claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        Gson gson = new Gson();
        User user = gson
                .fromJson(Jwts
                                .parser()
                                .setSigningKey(secretKey)
                                .parseClaimsJws(token)
                                .getBody()
                                .get("user")
                                .toString(),
                        User.class);
        return user.getUsername();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

}
