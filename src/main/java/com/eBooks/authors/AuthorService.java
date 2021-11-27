package com.eBooks.authors;

import com.eBooks.exceptions.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Author create(String name, String lastName) {
        return authorRepository.save(Author
                .builder()
                .name(name)
                .lastName(lastName)
                .books(new HashSet<>())
                .build());
    }

    public Author findById(Long authorId) throws AuthorNotFoundException {
        return authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);
    }
}
