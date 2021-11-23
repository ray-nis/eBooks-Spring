package com.eBooks.users;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eBooks.exceptions.JwtTokenMissingException;
import com.eBooks.shared.response.Response;
import com.eBooks.shared.response.ResponseFactory;
import com.eBooks.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final JWTUtil jwtUtil;
    private final UserService userService;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, JwtTokenMissingException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());

                DecodedJWT decodedJWT = jwtUtil.verifyToken(refresh_token);

                String username = decodedJWT.getSubject();
                User user = userService.findUserByUsername(username).get();

                Map<String, String> tokens = jwtUtil.createTokens(user, request);

                Response res = ResponseFactory.buildSuccessfulAuthenticationResponse(tokens);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.OK.value());

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules();
                objectMapper.writeValue(response.getOutputStream(), res);
            } catch (Exception exception) {
                Response res = ResponseFactory.buildFailAuthorizationResponse(exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(FORBIDDEN.value());

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules();
                objectMapper.writeValue(response.getOutputStream(), res);
            }
        } else {
            throw new JwtTokenMissingException();
        }
    }
}