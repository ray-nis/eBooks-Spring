package com.eBooks.books;

import com.eBooks.exceptions.AuthorNotFoundException;
import com.eBooks.exceptions.BookNotFoundException;
import com.eBooks.books.dto.BookPostDto;
import com.eBooks.shared.response.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/books/{id}")
    public ResponseEntity getBookById(@PathVariable("id") Long id) throws BookNotFoundException {
        return ResponseFactory.buildResponse(HttpStatus.OK, "", bookService.findByIdGetDto(id));
    }

    @PostMapping("/books")
    public ResponseEntity saveBook(@RequestBody BookPostDto bookPostDto) throws AuthorNotFoundException {
        return ResponseFactory.buildResponse(HttpStatus.OK, "", bookService.create(bookPostDto));
    }
}
