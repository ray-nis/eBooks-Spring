package com.eBooks.auth;

import com.eBooks.users.User;
import com.eBooks.users.UserRepository;
import com.eBooks.users.dto.UserSignupDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    void register_malformatedJSON() throws Exception {
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Malformed JSON request")));
    }

    @Test
    void register_success() throws Exception {
        userRepository.deleteAll();
        UserSignupDto userSignupDto = UserSignupDto
                .builder()
                .username("theusername")
                .password("thepassword")
                .matchingPassword("thepassword")
                .build();

        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successful signup")));
    }

    @Test
    void register_userExists() throws Exception {
        userRepository.deleteAll();
        UserSignupDto userSignupDto = UserSignupDto
                .builder()
                .username("theusername")
                .password("thepassword")
                .matchingPassword("thepassword")
                .build();

        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successful signup")));

        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Username already exists")));
    }

    @Test
    void register_passwordsDontMatch() throws Exception {
        UserSignupDto userSignupDto = UserSignupDto
                .builder()
                .username("theusername")
                .password("thepassword")
                .matchingPassword("themismatchedpassword")
                .build();

        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Passwords must match")));
    }

    @Test
    void register_usernameLimits() throws Exception {
        UserSignupDto userSignupDto = UserSignupDto
                .builder()
                .username("d")
                .password("thepassword")
                .matchingPassword("thepassword")
                .build();

        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Username must be a min of 3 and max of 20 characters")));
    }

    @Test
    void register_passwordLimits() throws Exception {
        UserSignupDto userSignupDto = UserSignupDto
                .builder()
                .username("username")
                .password("d")
                .matchingPassword("d")
                .build();

        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Password needs to be at least 6 characters")));
    }
}
