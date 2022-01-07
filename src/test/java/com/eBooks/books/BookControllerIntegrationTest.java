package com.eBooks.books;

import com.eBooks.authors.Author;
import com.eBooks.authors.AuthorRepository;
import com.eBooks.authors.AuthorService;
import com.eBooks.books.dto.BookGetDto;
import com.eBooks.books.dto.BookPostDto;
import com.eBooks.exceptions.AuthorNotFoundException;
import com.eBooks.exceptions.BookNotFoundException;
import com.eBooks.exceptions.GenreNotFoundException;
import com.eBooks.genres.Genre;
import com.eBooks.genres.GenreRepository;
import com.eBooks.genres.GenreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private GenreRepository genreRepository;

    BookPostDto bookPostDto;
    Author author;
    Genre genre;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        genreRepository.deleteAll();
        genre = genreService.create("TheGenre");
        author = authorRepository.save(Author
                .builder()
                .name("Author")
                .lastName("Last")
                .books(new HashSet<>())
                .build());

        bookPostDto = new BookPostDto();
        bookPostDto.setTitle("TheBook");
        bookPostDto.setDescription("TheDesc");
        bookPostDto.setTotalPages(100);
        bookPostDto.setPublishedDate(Date.from(Instant.now()));
        bookPostDto.setAuthorsId(new HashSet<>(Collections.singleton(author.getId())));
        bookPostDto.setGenresId(new HashSet<>(Collections.singleton(genre.getId())));
    }

    @Test
    @Transactional
    @WithMockUser(roles={"ADMIN"})
    public void postBook_success() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.data.title", is(bookPostDto.getTitle())))
                .andExpect(jsonPath("$.data.description", is(bookPostDto.getDescription())))
                .andExpect(jsonPath("$.data.totalPages", is(bookPostDto.getTotalPages())))
                .andDo(print())
                .andReturn();

        int returnedBookId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        Book book = bookService.findById((long) returnedBookId);
        assertEquals(bookPostDto.getTitle(), book.getTitle());
        assertEquals(bookPostDto.getDescription(), book.getDescription());
        assertEquals(bookPostDto.getTotalPages(), book.getTotalPages());
        assertEquals(true, book.getAuthors().contains(author));
        assertEquals(true, book.getGenres().contains(genre));
    }

    @Test
    @Transactional
    @WithMockUser(roles={"USER"})
    public void postBook_forbidden() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookPostDto)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Transactional
    public void getBook_success() throws Exception {
        BookGetDto bookGetDto = bookService.create(bookPostDto);
        MvcResult result = mockMvc.perform(get("/api/books/" + bookGetDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.data.title", is(bookPostDto.getTitle())))
                .andExpect(jsonPath("$.data.description", is(bookPostDto.getDescription())))
                .andExpect(jsonPath("$.data.totalPages", is(bookPostDto.getTotalPages())))
                .andDo(print())
                .andReturn();

        int returnedBookId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        Book book = bookService.findById((long) returnedBookId);
        assertEquals(bookPostDto.getTitle(), book.getTitle());
        assertEquals(bookPostDto.getDescription(), book.getDescription());
        assertEquals(bookPostDto.getTotalPages(), book.getTotalPages());
        assertEquals(true, book.getAuthors().contains(author));
        assertEquals(true, book.getGenres().contains(genre));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void deleteBook_success() throws Exception {
        BookGetDto bookGetDto = bookService.create(bookPostDto);
        MvcResult result = mockMvc.perform(delete("/api/books/" + bookGetDto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertThrows(BookNotFoundException.class,() -> bookService.findById(bookGetDto.getId()));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void updateBook_success() throws Exception {
        BookGetDto bookGetDto = bookService.create(bookPostDto);
        bookPostDto.setTitle("Changed");
        MvcResult result = mockMvc.perform(put("/api/books/" + bookGetDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.data.title", is("Changed")))
                .andExpect(jsonPath("$.data.description", is(bookPostDto.getDescription())))
                .andExpect(jsonPath("$.data.totalPages", is(bookPostDto.getTotalPages())))
                .andDo(print())
                .andReturn();

        int returnedBookId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        Book book = bookService.findById((long) returnedBookId);
        assertEquals("Changed", book.getTitle());
    }

    @Test
    public void getBooksByTitle_success() throws Exception {
        bookService.create(bookPostDto);
        MvcResult result = mockMvc.perform(get("/api/books?title=book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].title", is(bookPostDto.getTitle())))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getBooksByTitle_noBooks() throws Exception {
        bookService.create(bookPostDto);
        MvcResult result = mockMvc.perform(get("/api/books?title=n")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()", is(0)))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getBook_bookNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/books/3000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookPostDto)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertThrows(BookNotFoundException.class,() -> bookService.findById(3000L));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void deleteBook_bookNotFound() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/books/3000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookPostDto)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertThrows(BookNotFoundException.class,() -> bookService.findById(3000L));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void updateBook_bookNotFound() throws Exception {
        MvcResult result = mockMvc.perform(put("/api/books/3000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookPostDto)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertThrows(BookNotFoundException.class,() -> bookService.findById(3000L));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void postBook_authorNotFound() throws Exception {
        bookPostDto.setAuthorsId(new HashSet<>(Collections.singleton(3000L)));
        MvcResult result = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookPostDto)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertThrows(AuthorNotFoundException.class,() -> authorService.findById(3000L));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void postBook_genreNotFound() throws Exception {
        bookPostDto.setGenresId(new HashSet<>(Collections.singleton(3000L)));
        MvcResult result = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookPostDto)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertThrows(GenreNotFoundException.class,() -> genreService.findById(3000L));
    }
}