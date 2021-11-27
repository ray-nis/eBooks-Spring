package com.eBooks.books;

import com.eBooks.exceptions.AuthorNotFoundException;
import com.eBooks.exceptions.BookNotFoundException;
import com.eBooks.books.dto.BookPostDto;
import com.eBooks.shared.response.ResponseFactory;
import com.eBooks.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final MessageSourceUtil messageSourceUtil;

    @PostMapping("/books")
    public ResponseEntity saveBook(@RequestBody BookPostDto bookPostDto) throws AuthorNotFoundException {
        return ResponseFactory.buildResponse(HttpStatus.OK,
                messageSourceUtil.getMessage(messageSourceUtil.SUCCESS_CODE),
                bookService.create(bookPostDto));
    }

    @GetMapping("/books/{id}")
    public ResponseEntity getBookById(@PathVariable("id") Long id) throws BookNotFoundException {
        return ResponseFactory.buildResponse(HttpStatus.OK,
                messageSourceUtil.getMessage(messageSourceUtil.SUCCESS_CODE),
                bookService.findByIdGetDto(id));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity deleteBook(@PathVariable("id") Long id) throws BookNotFoundException {
        bookService.delete(id);
        return ResponseFactory.buildResponse(HttpStatus.OK,
                messageSourceUtil.getMessage(messageSourceUtil.SUCCESS_CODE));
    }
}
