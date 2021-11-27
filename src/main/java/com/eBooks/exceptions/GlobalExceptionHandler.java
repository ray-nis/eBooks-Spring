package com.eBooks.exceptions;

import com.eBooks.shared.response.Response;
import com.eBooks.shared.response.ResponseFactory;
import com.eBooks.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSourceUtil messageSourceUtil;

    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleAuthorNotFound() {
        return ResponseFactory.buildResponse(HttpStatus.NOT_FOUND, messageSourceUtil.getMessage("msg.error.author.notfouund"));
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleBookNotFound() {
        return ResponseFactory.buildResponse(HttpStatus.NOT_FOUND, messageSourceUtil.getMessage("msg.error.book.notfouund"));
    }

    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleUserExists() {
        return ResponseFactory.buildResponse(HttpStatus.BAD_REQUEST, messageSourceUtil.getMessage("msg.error.user.exists"));
    }

    @ExceptionHandler(JwtTokenMissingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleMissingJwtToken() {
        return ResponseFactory.buildResponse(HttpStatus.BAD_REQUEST, messageSourceUtil.getMessage("msg.error.jwt.missing"));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseFactory.buildResponse(HttpStatus.BAD_REQUEST, messageSourceUtil.getMessage("msg.error.json.malformed"));
    }
}
