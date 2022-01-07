package com.eBooks.genres;

import com.eBooks.books.dto.BookPostDto;
import com.eBooks.exceptions.AuthorNotFoundException;
import com.eBooks.exceptions.BookNotFoundException;
import com.eBooks.exceptions.GenreNotFoundException;
import com.eBooks.genres.dto.GenrePostDto;
import com.eBooks.shared.response.ResponseFactory;
import com.eBooks.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GenreController {
    private final MessageSourceUtil messageSourceUtil;
    private final GenreService genreService;

    @GetMapping("/genres")
    public ResponseEntity getGenres() {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                genreService.getGenres()
        );
    }

    @PostMapping("/genres")
    public ResponseEntity saveGenre(@RequestBody GenrePostDto genrePostDto) {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                genreService.create(genrePostDto.getName()));
    }

    @PutMapping("/genres/{id}")
    public ResponseEntity updateGenre(@PathVariable("id") Long id, @RequestBody GenrePostDto genrePostDto) throws GenreNotFoundException {
        return ResponseFactory.ok(
                messageSourceUtil.success(),
                genreService.update(id, genrePostDto));
    }
}
