package com.eBooks.shared.dev;

import com.eBooks.authors.Author;
import com.eBooks.authors.AuthorService;
import com.eBooks.books.Book;
import com.eBooks.books.BookRepository;
import com.eBooks.books.BookService;
import com.eBooks.genres.Genre;
import com.eBooks.genres.GenreRepository;
import com.eBooks.genres.GenreService;
import com.eBooks.users.RoleService;
import com.eBooks.users.UserService;
import com.eBooks.users.dto.UserSignupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;

@Profile("dev")
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final GenreService genreService;
    private final BookService bookService;
    private final AuthorService authorService;

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        roleService.create("ROLE_USER");

        userService.save(UserSignupDto.builder()
                .username("FirstUser")
                .password("password")
                .matchingPassword("password")
                .build());

        Genre genre1 = genreService.create("Genre1");
        Book book1 = bookService.create("Book1");
        Author author1 = authorService.create("author", "one");

        book1.getAuthors().add(author1);
        bookService.addGenre(book1, genre1);

        //System.out.println(book1.getGenres());
        //System.out.println(bookRepository.findAllByGenresIn(new HashSet<>(Collections.singleton(genre1))));
    }
}
