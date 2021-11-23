package com.eBooks.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eBooks.users.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTUtil {
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Algorithm algorithm;
    private static final int ACCESS_TOKEN_EXPIRE_TIME = 1 * (60 * 60);
    private static final int REFRESH_TOKEN_EXPIRE_TIME = (31 * 24 ) * (60 * 60);

    public Map<String, String> createTokens(User user, HttpServletRequest request) {
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRE_TIME)))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRE_TIME)))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        return tokens;
    }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    @PostConstruct
    public void setUpAlgo() {
        algorithm = Algorithm.HMAC256(secretKey);
    }
}
