package com.eBooks.books;

import com.eBooks.authors.Author;
import com.eBooks.authors.AuthorRepository;
import com.eBooks.books.dto.BookGetDto;
import com.eBooks.books.dto.BookPostDto;
import com.eBooks.exceptions.AuthorNotFoundException;
import com.eBooks.exceptions.BookNotFoundException;
import com.eBooks.users.UserRepository;
import com.eBooks.users.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

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
    BookRepository bookRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookService bookService;

    BookPostDto bookPostDto;
    Author author;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
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
    }

    @Test
    @Transactional
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
    }

    @Test
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
}