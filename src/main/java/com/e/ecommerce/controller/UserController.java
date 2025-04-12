package com.e.ecommerce.controller;

import com.e.ecommerce.dto.UserDTO;
import com.e.ecommerce.service.UserAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Handles user authentication and registration")
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserAuth userAuth;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userAuth.register(user));
    }

    @Operation(summary = "Login user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO request) {
        return ResponseEntity.ok(userAuth.login(request));
    }
}
