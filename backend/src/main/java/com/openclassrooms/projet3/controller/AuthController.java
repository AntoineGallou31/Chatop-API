// Java
package com.openclassrooms.projet3.controller;

import com.openclassrooms.projet3.dto.request.LoginDto;
import com.openclassrooms.projet3.dto.request.RegisterUserDto;
import com.openclassrooms.projet3.dto.response.JwtResponse;
import com.openclassrooms.projet3.dto.response.UserResponseDto;
import com.openclassrooms.projet3.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations related to user authentication and registration")
public class AuthController {

    private final AuthService authService;

    // Endpoint to register a new user
    @PostMapping("/register")
    @Operation(summary = "Register a new user", responses = {
            @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public JwtResponse register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return this.authService.register(registerUserDto);
    }

    // Endpoint to authenticate a user and return a JWT token
    @PostMapping("/login")
    @Operation(summary = "Login user", responses = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    })
    public JwtResponse login(@Valid @RequestBody LoginDto loginDto) {
        return this.authService.authenticate(loginDto);
    }

    // Endpoint to retrieve information of the currently authenticated user
    @GetMapping("/me")
    @Operation(summary = "Get user information", responses = {
            @ApiResponse(responseCode = "200", description = "User information retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid token")
    })
    public UserResponseDto getUser(Authentication authentication){
        return this.authService.getUser(authentication);
    }
}