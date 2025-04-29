package com.example.demo.controller;

import com.example.demo.models.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        // Arrange
        User user1 = new User();
        User user2 = new User();
        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(mockUsers);

        // Act
        List<User> users = userController.getUsers();

        // Assert
        assertEquals(2, users.size());
        assertEquals(mockUsers, users);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUser_Found() {
        // Arrange
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<User> response = userController.getUser(email);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void testGetUser_NotFound() {
        // Arrange
        String email = "missing@example.com";

        when(userService.getUserByEmail(email)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.getUser(email);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("new@example.com");

        when(userService.registerUser(newUser)).thenReturn(true);

        // Act
        ResponseEntity<String> response = userController.registerUser(newUser);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully!", response.getBody());
        verify(userService, times(1)).registerUser(newUser);
    }

    @Test
    void testRegisterUser_EmailExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");

        when(userService.registerUser(existingUser)).thenReturn(false);

        // Act
        ResponseEntity<String> response = userController.registerUser(existingUser);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User with this email already exists!", response.getBody());
        verify(userService, times(1)).registerUser(existingUser);
    }

    @Test
    void testLoginUser_Success() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        User loginRequest = new User();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        User mockUser = new User();
        mockUser.setEmail(email);

        when(userService.login(email, password)).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<User> response = userController.loginUser(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).login(email, password);
    }

    @Test
    void testLoginUser_Unauthorized() {
        // Arrange
        String email = "wrong@example.com";
        String password = "wrongpassword";
        User loginRequest = new User();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        when(userService.login(email, password)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.loginUser(loginRequest);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(userService, times(1)).login(email, password);
    }

    @Test
    void testSignup_Success() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("signup@example.com");

        when(userService.registerUser(newUser)).thenReturn(true);

        // Act
        ResponseEntity<String> response = userController.signup(newUser);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully!", response.getBody());
        verify(userService, times(1)).registerUser(newUser);
    }

    @Test
    void testSignup_EmailExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("exists@example.com");

        when(userService.registerUser(existingUser)).thenReturn(false);

        // Act
        ResponseEntity<String> response = userController.signup(existingUser);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User with this email already exists!", response.getBody());
        verify(userService, times(1)).registerUser(existingUser);
    }
}
