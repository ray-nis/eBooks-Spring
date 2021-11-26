package com.eBooks.books;

import com.eBooks.exceptions.BookNotFoundException;
import com.eBooks.genres.Genre;
import com.eBooks.mapstruct.dtos.BookGetDto;
import com.eBooks.mapstruct.mappers.MapStructMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final MapStructMapper mapStructMapper;

    public Book create(String name) {
        return bookRepository.save(
                Book
                        .builder()
                        .title(name)
                        .totalPages(10)
                        .publishedDate(Instant.now())
                        //.genres(new HashSet<>())
                        .authors(new HashSet<>())
                        .build());
    }

    @Transactional
    public Book addGenre(Book book, Genre genre) {
        //book.getGenres().add(genre);
        return bookRepository.save(book);
    }

    public Book findById(Long id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    public BookGetDto findByIdGetDto(Long id) throws BookNotFoundException {
        return mapStructMapper.bookToBookGetDto(findById(id));
    }
}
