package com.example.demo.controller;

import com.example.demo.models.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        boolean created = userService.registerUser(user);
        return created
                ? ResponseEntity.ok("User registered successfully!")
                : ResponseEntity.badRequest().body("User with this email already exists!");
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        Optional<User> foundUser = userService.login(user.getEmail(), user.getPassword());
        return foundUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(401).build());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        boolean created = userService.registerUser(user);
        return created
                ? ResponseEntity.ok("User registered successfully!")
                : ResponseEntity.badRequest().body("User with this email already exists!");
    }


}
