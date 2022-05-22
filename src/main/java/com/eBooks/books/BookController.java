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

import java.util.Optional;

@RestController()
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final MessageSourceUtil messageSourceUtil;

    @GetMapping("/books")
    public ResponseEntity getBooks(@RequestParam("page") Optional<Integer> page, @RequestParam("sort") Optional<String> sort) throws BookNotFoundException {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                bookService.findBooks(page, sort));
    }

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

    @RequestMapping(value = "/books", params = "genreId", method = RequestMethod.GET)
    public ResponseEntity getBooksByGenreId(@RequestParam("genreId") Long genreId, @RequestParam("page") Optional<Integer> page, @RequestParam("sort") Optional<String> sort) throws GenreNotFoundException {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                bookService.findByGenre(genreId, page, sort));
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
