package com.example.demo.controller;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(value = AuthController.class, excludeAutoConfiguration = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void signup_NewUser_ReturnsSuccess() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setUserType("user");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        String userJson = """
            {
              "email": "test@example.com",
              "password": "password",
              "userType": "user"
            }
            """;

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
    void signup_ExistingEmail_ReturnsBadRequest() throws Exception {
        User existingUser = new User();
        existingUser.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));

        String userJson = """
            {
              "email": "test@example.com",
              "password": "password",
              "userType": "user"
            }
            """;

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists!"));
    }

    @Test
    void loginUser_ValidCredentials_ReturnsOk() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setUserType("user");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        String loginJson = """
            {
              "email": "test@example.com",
              "password": "password",
              "userType": "user"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk());
    }

    @Test
    void loginUser_InvalidPassword_ReturnsUnauthorized() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("correct_password");
        user.setUserType("user");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        String loginJson = """
            {
              "email": "test@example.com",
              "password": "wrong_password",
              "userType": "user"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginUser_InvalidUserType_ReturnsUnauthorized() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setUserType("admin"); // User type mismatch!

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        String loginJson = """
            {
              "email": "test@example.com",
              "password": "password",
              "userType": "user"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized());
    }
}