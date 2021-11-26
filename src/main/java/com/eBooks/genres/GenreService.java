package com.eBooks.genres;

import com.eBooks.books.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public Genre create(String name) {
        return genreRepository.save(
                Genre
                        .builder()
                        .name( name)
                        .books(new HashSet<>())
                        .build()
        );
    }
}
