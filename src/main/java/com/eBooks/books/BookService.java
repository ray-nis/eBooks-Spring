package com.eBooks.books;

import com.eBooks.authors.Author;
import com.eBooks.authors.AuthorService;
import com.eBooks.books.dto.BookSlimDto;
import com.eBooks.exceptions.AuthorNotFoundException;
import com.eBooks.exceptions.BookNotFoundException;
import com.eBooks.exceptions.GenreNotFoundException;
import com.eBooks.genres.Genre;
import com.eBooks.books.dto.BookGetDto;
import com.eBooks.books.dto.BookPostDto;
import com.eBooks.genres.GenreService;
import com.eBooks.mapstruct.mappers.MapStructMapper;
import com.eBooks.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final MapStructMapper mapStructMapper;
    private final AuthorService authorService;
    private final GenreService genreService;

    public Book create(String name) {
        return bookRepository.save(
                Book
                        .builder()
                        .title(name)
                        .totalPages(10)
                        .publishedDate(Instant.now())
                        .genres(new HashSet<>())
                        .authors(new HashSet<>())
                        .build());
    }

    public BookGetDto create(BookPostDto bookPostDto) throws AuthorNotFoundException, GenreNotFoundException {
        HashSet<Author> authors = getAuthors(bookPostDto.getAuthorsId());
        HashSet<Genre> genres = getGenres(bookPostDto.getGenresId());

        Book book = bookRepository.save(
                Book
                        .builder()
                        .title(bookPostDto.getTitle())
                        .description(bookPostDto.getDescription())
                        .isbn(bookPostDto.getIsbn())
                        .totalPages(bookPostDto.getTotalPages())
                        .publishedDate(bookPostDto.getPublishedDate().toInstant())
                        .genres(genres)
                        .authors(authors)
                        .build());
        return mapStructMapper.bookToBookGetDto(book);
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

    public void delete(Long id) throws BookNotFoundException {
        Book book = findById(id);
        bookRepository.delete(book);
    }

    @Transactional
    public BookGetDto update(Long id, BookPostDto bookPostDto) throws BookNotFoundException, AuthorNotFoundException {
        Book book = findById(id);
        book.setTitle(bookPostDto.getTitle());
        book.setDescription(bookPostDto.getDescription());
        book.setTotalPages(bookPostDto.getTotalPages());
        book.setIsbn(bookPostDto.getIsbn());
        book.setPublishedDate(bookPostDto.getPublishedDate().toInstant());
        book.setAuthors(getAuthors(bookPostDto.getAuthorsId()));
        book.setCreatedAt(book.getCreatedAt());
        book.setUpdatedAt(Instant.now());
        Book savedBook = bookRepository.save(book);
        return mapStructMapper.bookToBookGetDto(savedBook);
    }

    private HashSet<Author> getAuthors(Set<Long> authorsId) throws AuthorNotFoundException {
        HashSet<Author> authors = new HashSet<>();
        for (Long authorId : authorsId) {
            authors.add(authorService.findById(authorId));
        }

        return authors;
    }

    private HashSet<Genre> getGenres(Set<Long> genresId) throws GenreNotFoundException {
        HashSet<Genre> genres = new HashSet<>();
        for (Long genreId : genresId) {
            genres.add(genreService.findById(genreId));
        }

        return genres;
    }

    @Transactional
    public List<BookGetDto> findByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return books.stream().map(book -> mapStructMapper.bookToBookGetDto(book)).collect(Collectors.toList());
    }

    public List<BookSlimDto> findBooks(Optional<Integer> page, Optional<String> sort) {
        Pageable pageable = PaginationUtil.getAllBooksPageable(page, sort);
        Page<Book> books = bookRepository.findAll(pageable);
        return books.stream().map(book -> mapStructMapper.bookToBookSlimDto(book)).collect(Collectors.toList());
    }
}
