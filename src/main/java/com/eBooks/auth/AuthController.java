package com.eBooks.auth;

import com.eBooks.shared.response.Response;
import com.eBooks.users.UserService;
import com.eBooks.users.dto.UserSignupDto;
import com.eBooks.shared.response.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;

    @GetMapping("/home")
    public ResponseEntity home() {
        return ResponseFactory.buildResponse(HttpStatus.OK, "", "Hello");
    }

    @PostMapping("/signup")
    public ResponseEntity register(@Valid @RequestBody UserSignupDto userSignupDto, BindingResult result) throws Exception {
        if (result.hasFieldErrors()) {
            return ResponseFactory.buildResponse(HttpStatus.BAD_REQUEST, result.getFieldError().getDefaultMessage());
        }
        if (result.hasGlobalErrors()) {
            return ResponseFactory.buildResponse(HttpStatus.BAD_REQUEST, result.getGlobalError().getDefaultMessage());
        }


        userService.save(userSignupDto);
        return ResponseFactory.buildResponse(HttpStatus.OK);
    }
}
