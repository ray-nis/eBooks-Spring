package com.eBooks.shared.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class Response {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private int code;

    private HttpStatus status;

    private String message;

    private Object data;

    public Response() {
        timestamp = LocalDateTime.now();
    }

    public Response(HttpStatus httpStatus) {
        this();

        this.code = httpStatus.value();
        this.status = httpStatus;
        this.message = "";
    }

    public Response(HttpStatus httpStatus, String message) {
        this();

        this.code = httpStatus.value();
        this.status = httpStatus;
        this.message = message;
    }

    public Response(HttpStatus httpStatus, String message, Object data) {
        this();

        this.code = httpStatus.value();
        this.status = httpStatus;
        this.message = message;
        this.data = data;
    }
}
