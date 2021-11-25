package com.eBooks.security.filters;

import com.eBooks.users.UserRepository;
import com.eBooks.users.UserService;
import com.eBooks.users.dto.UserSignupDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationFilterIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void alive_success() throws Exception {
        userService.save(UserSignupDto
                .builder()
                .username("praprapra")
                .password("praprapra")
                .build());

        MvcResult result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "praprapra")
                .param("password", "praprapra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", notNullValue()))
                .andReturn();

        String accessToken = JsonPath.read(result.getResponse().getContentAsString(), "$.data.access_token");

        mockMvc.perform(get("/api/alive")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", is("alive")))
            .andDo(print());
    }

    @Test
    void aliveJwtMissing_fail() throws Exception {
        userService.save(UserSignupDto
                .builder()
                .username("praprapra")
                .password("praprapra")
                .build());

        MvcResult result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "praprapra")
                .param("password", "praprapra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", notNullValue()))
                .andReturn();

        String accessToken = JsonPath.read(result.getResponse().getContentAsString(), "$.data.access_token");

        mockMvc.perform(get("/api/alive")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void aliveFakeToken_fail() throws Exception {
        userService.save(UserSignupDto
                .builder()
                .username("praprapra")
                .password("praprapra")
                .build());

        MvcResult result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "praprapra")
                .param("password", "praprapra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", notNullValue()))
                .andReturn();

        String accessToken = JsonPath.read(result.getResponse().getContentAsString(), "$.data.access_token");

        String fakeAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        mockMvc.perform(get("/api/alive")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + fakeAccessToken))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}