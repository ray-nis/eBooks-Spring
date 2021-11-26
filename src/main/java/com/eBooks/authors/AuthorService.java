package com.eBooks.authors;

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
}
