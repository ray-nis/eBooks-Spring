package com.eBooks.books;

import com.eBooks.exceptions.AuthorNotFoundException;
import com.eBooks.exceptions.BookNotFoundException;
import com.eBooks.books.dto.BookPostDto;
import com.eBooks.exceptions.GenreNotFoundException;
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
    public ResponseEntity saveBook(@RequestBody BookPostDto bookPostDto) throws AuthorNotFoundException, GenreNotFoundException {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                bookService.create(bookPostDto));
    }

    @RequestMapping(value = "/books", params = "title", method = RequestMethod.GET)
    public ResponseEntity getBooksByTitle(@RequestParam("title") String title) {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                bookService.findByTitle(title));
    }

    @GetMapping("/books/{id}")
    public ResponseEntity getBookById(@PathVariable("id") Long id) throws BookNotFoundException {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                bookService.findByIdGetDto(id));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity deleteBook(@PathVariable("id") Long id) throws BookNotFoundException {
        bookService.delete(id);
        return ResponseFactory.ok(
                messageSourceUtil.success());
    }

    @PutMapping("/books/{id}")
    public ResponseEntity updateBook(@PathVariable("id") Long id, @RequestBody BookPostDto bookPostDto) throws BookNotFoundException, AuthorNotFoundException {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                bookService.update(id, bookPostDto));
    }
}
