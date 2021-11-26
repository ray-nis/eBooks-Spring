package com.eBooks.books;

import com.eBooks.exceptions.BookNotFoundException;
import com.eBooks.mapstruct.mappers.MapStructMapper;
import com.eBooks.shared.response.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/book/{id}")
    public ResponseEntity getBookById(@PathVariable("id") Long id) throws BookNotFoundException {
        return ResponseFactory.buildResponse(HttpStatus.OK, "", bookService.findByIdGetDto(id));
    }
}
