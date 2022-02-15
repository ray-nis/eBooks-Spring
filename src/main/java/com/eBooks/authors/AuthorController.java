package com.eBooks.authors;

import com.eBooks.authors.dto.AuthorPostDto;
import com.eBooks.books.dto.BookPostDto;
import com.eBooks.exceptions.AuthorNotFoundException;
import com.eBooks.exceptions.BookNotFoundException;
import com.eBooks.exceptions.GenreNotFoundException;
import com.eBooks.shared.response.ResponseFactory;
import com.eBooks.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthorController {
    private final AuthorService authorService;
    private final MessageSourceUtil messageSourceUtil;

    @GetMapping("/authors/{id}")
    public ResponseEntity findById(@PathVariable("id") Long id) throws AuthorNotFoundException {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                authorService.findByIdGetDto(id)
        );
    }

    @GetMapping("/authors")
    public ResponseEntity findAuthors() throws AuthorNotFoundException {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                authorService.findAuthors()
        );
    }

    @RequestMapping(value = "/authors", params = "name", method = RequestMethod.GET)
    public ResponseEntity getAuthorsByName(@RequestParam("name") String name) {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                authorService.findByName(name));
    }

    @RequestMapping(value = "/authors", params = "lastname", method = RequestMethod.GET)
    public ResponseEntity getAuthorsByLastName(@RequestParam("lastname") String lastname) {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                authorService.findByLastName(lastname));
    }

    @RequestMapping(value = "/authors", params = "fullname", method = RequestMethod.GET)
    public ResponseEntity getAuthorsByFullName(@RequestParam("fullname") String fullname) {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                authorService.findByFullName(fullname));
    }

    @PostMapping("/authors")
    public ResponseEntity saveAuthor(@RequestBody AuthorPostDto authorPostDto) {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                authorService.create(authorPostDto));
    }

    @PutMapping("/authors/{id}")
    public ResponseEntity updateAuthor(@PathVariable("id") Long id, @RequestBody AuthorPostDto authorPostDto) throws AuthorNotFoundException {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                authorService.update(id, authorPostDto));
    }
}
