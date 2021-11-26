package com.eBooks.authors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/author/{id}")
    public ResponseEntity findById(@PathVariable("id") Long id) {
        return null;
    }
}
