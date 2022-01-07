package com.eBooks.genres;

import com.eBooks.books.Book;
import com.eBooks.books.BookRepository;
import com.eBooks.books.dto.BookGetDto;
import com.eBooks.genres.dto.GenrePostDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.h2.util.TempFileDeleter;
import org.junit.jupiter.api.AfterEach;
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

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GenreControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private GenreService genreService;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        genreRepository.deleteAll();
    }

    @Test
    public void getGenres_success() throws Exception {
        genreService.create("One");
        genreService.create("Two");
        MvcResult result = mockMvc.perform(get("/api/genres")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.data.[0].name", is("One")))
                .andExpect(jsonPath("$.data.[1].name", is("Two")))
                .andExpect(jsonPath("$.data.length()", is(2)))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Transactional
    @WithMockUser(roles={"ADMIN"})
    public void postGenre_success() throws Exception {
        GenrePostDto genreDto = new GenrePostDto();
        genreDto.setName("NewGenre");

        MvcResult result = mockMvc.perform(post("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.data.name", is(genreDto.getName())))
                .andDo(print())
                .andReturn();

        int returnedGenreId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        Genre genre = genreService.findById((long) returnedGenreId);
        assertEquals(genreDto.getName(), genre.getName());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void updateGenre_success() throws Exception {
        GenrePostDto genreDto = new GenrePostDto();
        genreDto.setName("NewGenre");
        Genre genre = genreService.create(genreDto.getName());
        genreDto.setName("Changed");
        MvcResult result = mockMvc.perform(put("/api/genres/" + genre.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.data.name", is("Changed")))
                .andDo(print())
                .andReturn();

        int returnedGenreId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        Genre genre1 = genreService.findById((long) returnedGenreId);
        assertEquals(genreDto.getName(), genre1.getName());
    }

    @Test
    @Transactional
    @WithMockUser(roles={"USER"})
    public void postGenre_forbidden() throws Exception {
        GenrePostDto genreDto = new GenrePostDto();
        genreDto.setName("NewGenre");

        MvcResult result = mockMvc.perform(post("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }
}