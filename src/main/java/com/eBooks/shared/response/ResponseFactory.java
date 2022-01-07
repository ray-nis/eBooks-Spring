package com.eBooks.shared.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory {
    public static ResponseEntity buildResponse(Response response) {
        return new ResponseEntity(response, response.getStatus());
    }

    public static ResponseEntity buildResponse(HttpStatus httpStatus) {
        Response response = new Response(httpStatus);
        return buildResponse(response);
    }

    public static ResponseEntity buildResponse(HttpStatus httpStatus, String message) {
        Response response = new Response(httpStatus, message);
        return buildResponse(response);
    }

    public static ResponseEntity buildResponse(HttpStatus httpStatus, String message, Object data) {
        Response response = new Response(httpStatus, message, data);
        return buildResponse(response);
    }

    public static Response buildSuccessfulAuthenticationResponse(Object data) {
        return new Response(HttpStatus.OK, "", data);
    }

    public static Response buildFailAuthorizationResponse(String message) {
        return new Response(HttpStatus.FORBIDDEN, message);
    }

    public static ResponseEntity ok(String message, Object data) {
        Response response = new Response(HttpStatus.OK, message, data);
        return buildResponse(response);
    }

    public static ResponseEntity ok(String message) {
        Response response = new Response(HttpStatus.OK, message);
        return buildResponse(response);
    }
}
