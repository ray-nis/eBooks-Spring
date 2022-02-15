package com.eBooks.authors;

import com.eBooks.authors.dto.AuthorGetDto;
import com.eBooks.authors.dto.AuthorPostDto;
import com.eBooks.authors.dto.AuthorSlimDto;
import com.eBooks.books.Book;
import com.eBooks.books.dto.BookGetDto;
import com.eBooks.exceptions.AuthorNotFoundException;
import com.eBooks.mapstruct.mappers.MapStructMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final MapStructMapper mapStructMapper;

    public Author create(String name, String lastName) {
        return authorRepository.save(Author
                .builder()
                .name(name)
                .lastName(lastName)
                .fullName(name + " " + lastName)
                .books(new HashSet<>())
                .build());
    }

    public AuthorGetDto create(AuthorPostDto authorPostDto) {
        return mapStructMapper.authorToAuthorGetDto(create(authorPostDto.getName(), authorPostDto.getLastName()));
    }

    public Author findById(Long authorId) throws AuthorNotFoundException {
        return authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);
    }

    public AuthorGetDto findByIdGetDto(Long id) throws AuthorNotFoundException {
        return mapStructMapper.authorToAuthorGetDto(findById(id));
    }

    public List<AuthorGetDto> findByName(String name) {
        List<Author> authors = authorRepository.findByNameContainingIgnoreCase(name);
        return authors.stream().map(author -> mapStructMapper.authorToAuthorGetDto(author)).collect(Collectors.toList());
    }

    public List<AuthorGetDto> findByLastName(String lastname) {
        List<Author> authors = authorRepository.findByLastNameContainingIgnoreCase(lastname);
        return authors.stream().map(author -> mapStructMapper.authorToAuthorGetDto(author)).collect(Collectors.toList());
    }

    public List<AuthorGetDto> findByFullName(String fullname) {
        List<Author> authors = authorRepository.findByFullNameContainingIgnoreCase(fullname);
        return authors.stream().map(author -> mapStructMapper.authorToAuthorGetDto(author)).collect(Collectors.toList());
    }

    @Transactional
    public AuthorGetDto update(Long id, AuthorPostDto authorPostDto) throws AuthorNotFoundException {
        Author author = findById(id);

        author.setName(authorPostDto.getName());
        author.setLastName(authorPostDto.getLastName());
        author.setFullName(authorPostDto.getName() + " " + authorPostDto.getLastName());
        return mapStructMapper.authorToAuthorGetDto(authorRepository.save(author));
    }

    public List<AuthorSlimDto> findAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().map(author -> mapStructMapper.authorToAuthorSlimDto(author)).collect(Collectors.toList());
    }
}
