package com.eBooks.authors;

import com.eBooks.authors.dto.AuthorGetDto;
import com.eBooks.authors.dto.AuthorPostDto;
import com.eBooks.books.Book;
import com.eBooks.books.BookRepository;
import com.eBooks.books.dto.BookGetDto;
import com.eBooks.exceptions.AuthorNotFoundException;
import com.eBooks.exceptions.BookNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.h2.util.TempFileDeleter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;

    AuthorPostDto authorPostDto;

    @BeforeEach
    @Transactional
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        authorPostDto = new AuthorPostDto();
        authorPostDto.setName("New");
        authorPostDto.setLastName("Nuevo");
    }

    @Test
    @Transactional
    @WithMockUser(roles={"ADMIN"})
    public void postAuthor_success() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.data.name", is(authorPostDto.getName())))
                .andExpect(jsonPath("$.data.lastName", is(authorPostDto.getLastName())))
                .andDo(print())
                .andReturn();

        int returnedAuthorId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        Author author = authorService.findById((long) returnedAuthorId);
        assertEquals(authorPostDto.getName(), author.getName());
        assertEquals(authorPostDto.getLastName(), author.getLastName());
    }

    @Test
    @Transactional
    @WithMockUser(roles={"USER"})
    public void postAuthor_forbidden() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorPostDto)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Transactional
    @WithMockUser(roles={"ADMIN"})
    public void updateAuthor_success() throws Exception {
        Author author = authorService.create(authorPostDto.getName(), authorPostDto.getLastName());
        authorPostDto.setName("Changed");
        MvcResult result = mockMvc.perform(put("/api/authors/" + author.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.data.name", is("Changed")))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Transactional
    public void getAuthor_success() throws Exception {
        AuthorGetDto authorGetDto = authorService.create(authorPostDto);
        MvcResult result = mockMvc.perform(get("/api/authors/" + authorGetDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorGetDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.data.name", is(authorGetDto.getName())))
                .andExpect(jsonPath("$.data.lastName", is(authorGetDto.getLastName())))
                .andDo(print())
                .andReturn();

        int returnedAuthorId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        Author author = authorService.findById((long) returnedAuthorId);
        assertEquals(authorPostDto.getName(), author.getName());
        assertEquals(authorPostDto.getLastName(), author.getLastName());
    }

    @Test
    @Transactional
    public void getAuthor_authorNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/books/3000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorPostDto)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertThrows(AuthorNotFoundException.class,() -> authorService.findById(3000L));
    }

    @Test
    public void getAuthorsByName_noAuthors() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/authors?name=new")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()", is(0)))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getAuthorsByName_success() throws Exception {
        authorService.create(authorPostDto);
        MvcResult result = mockMvc.perform(get("/api/authors?name=new")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].name", is(authorPostDto.getName())))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getAuthorsByLastName_success() throws Exception {
        authorService.create(authorPostDto);
        MvcResult result = mockMvc.perform(get("/api/authors?lastname=nuevo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].lastName", is(authorPostDto.getLastName())))
                .andDo(print())
                .andReturn();
    }
}