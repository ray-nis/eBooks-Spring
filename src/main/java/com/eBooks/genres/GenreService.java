package com.eBooks.genres;

import com.eBooks.exceptions.GenreNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Genre findById(Long genreId) throws GenreNotFoundException {
        return genreRepository.findById(genreId).orElseThrow(GenreNotFoundException::new);
    }
}
