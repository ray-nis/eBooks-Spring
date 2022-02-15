package com.eBooks.shared.dev;

import com.eBooks.authors.Author;
import com.eBooks.authors.AuthorService;
import com.eBooks.books.Book;
import com.eBooks.books.BookRepository;
import com.eBooks.books.BookService;
import com.eBooks.books.dto.BookGetDto;
import com.eBooks.books.dto.BookPostDto;
import com.eBooks.genres.Genre;
import com.eBooks.genres.GenreRepository;
import com.eBooks.genres.GenreService;
import com.eBooks.users.RoleService;
import com.eBooks.users.User;
import com.eBooks.users.UserService;
import com.eBooks.users.dto.UserSignupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

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
        roleService.create("ROLE_ADMIN");

        userService.save(UserSignupDto.builder()
                .username("FirstUser")
                .password("password")
                .matchingPassword("password")
                .build());

        roleService.makeAdmin(userService.save(UserSignupDto.builder()
                .username("AdminUser")
                .password("password")
                .matchingPassword("password")
                .build()));

        Genre genre1 = genreService.create("Genre1");
        Genre genre2 = genreService.create("Genre2");
        Long[] genres = {genre1.getId(), genre2.getId()};
        Author author1 = authorService.create("author", "one");
        Author author2 = authorService.create("author", "two");
        Long[] authors = {author1.getId(), author2.getId()};

        BookPostDto book1PostDto = new BookPostDto();
        book1PostDto.setTitle("TheBook");
        book1PostDto.setDescription("TheDesc");
        book1PostDto.setTotalPages(100);
        book1PostDto.setPublishedDate(Date.from(Instant.now()));
        book1PostDto.setAuthorsId(new HashSet<Long>(Arrays.asList(authors)));
        book1PostDto.setGenresId(new HashSet<>(Arrays.asList(genres)));

        BookGetDto book1 = bookService.create(book1PostDto);

        for (int i = 0; i < 50; i++) {
            BookPostDto bookPostDto = new BookPostDto();
            bookPostDto.setTitle("TheBook " + i);
            bookPostDto.setDescription("TheDesc " + i);
            bookPostDto.setTotalPages(100);
            bookPostDto.setPublishedDate(Date.from(Instant.now()));
            bookPostDto.setAuthorsId(new HashSet<Long>(Arrays.asList(authors)));
            bookPostDto.setGenresId(new HashSet<>(Arrays.asList(genres)));

            bookService.create(bookPostDto);
        }



        //System.out.println(book1.getGenres());
        //System.out.println(bookRepository.findAllByGenresIn(new HashSet<>(Collections.singleton(genre1))));
    }
}
