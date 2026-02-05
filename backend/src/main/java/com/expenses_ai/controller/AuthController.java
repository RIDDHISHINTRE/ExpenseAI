package com.expenses_ai.controller;

import com.expenses_ai.dto.RegisterRequest;
import com.expenses_ai.dto.LoginRequest;
import com.expenses_ai.model.User;
import com.expenses_ai.service.UserService;
import com.expenses_ai.config.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {

        User registeredUser = userService.register(registerRequest);

        if (registeredUser == null) {
            return ResponseEntity.badRequest().body("User already exists with this email!");
        }

        registeredUser.setPassword(null);  // hide password
        return ResponseEntity.status(201).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        User loggedInUser = userService.login(loginRequest);

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Invalid email or password!");
        }

        // Generate JWT Token
        String token = jwtUtil.generateToken(loggedInUser.getEmail());

        loggedInUser.setPassword(null); // hide password

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("user", loggedInUser);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
