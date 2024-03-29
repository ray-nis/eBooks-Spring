package com.eBooks.auth;

import com.eBooks.users.UserService;
import com.eBooks.users.dto.UserSignupDto;
import com.eBooks.shared.response.ResponseFactory;
import com.eBooks.util.MessageSourceUtil;
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
    private final MessageSourceUtil messageSourceUtil;

    @GetMapping("/alive")
    public ResponseEntity home() {
        return ResponseFactory.buildResponse(HttpStatus.OK, "", "alive");
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
        return ResponseFactory.buildResponse(HttpStatus.OK, messageSourceUtil.getMessage("msg.success.signup"));
    }
}
