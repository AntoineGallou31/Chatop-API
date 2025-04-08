// Java
package com.openclassrooms.projet3.service.impl;

import com.openclassrooms.projet3.dto.request.LoginDto;
import com.openclassrooms.projet3.dto.request.RegisterUserDto;
import com.openclassrooms.projet3.dto.response.JwtResponse;
import com.openclassrooms.projet3.dto.response.UserResponseDto;
import com.openclassrooms.projet3.entity.User;
import com.openclassrooms.projet3.repository.UserRepository;
import com.openclassrooms.projet3.security.JwtProvider;
import com.openclassrooms.projet3.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // Registers a new user and returns a JWT token response
    public JwtResponse register(RegisterUserDto registerUserDto) {
        // Build a new User object using the builder pattern
        var user = User.builder()
                .name(registerUserDto.getName()) // Sets the user's name
                .email(registerUserDto.getEmail()) // Sets the user's email
                .password(passwordEncoder.encode(registerUserDto.getPassword())) // Hashes the password
                .build();

        userRepository.save(user); // Saves the user to the database

        // Generates a JWT token for the new user
        String jwtToken = jwtProvider.generateToken(user);

        return new JwtResponse(jwtToken);
    }

    // Authenticates an existing user and returns a JWT token response
    public JwtResponse authenticate(LoginDto loginDto) {
        // Retrieve the user using the provided email
        var user = userRepository.findByEmail(loginDto.getEmail());

        // Check if the provided password matches the stored hashed password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Generates a JWT token if credentials are valid
        String jwtToken = jwtProvider.generateToken(user);

        return new JwtResponse(jwtToken);
    }

    // Retrieves user information based on the current authentication context
    public UserResponseDto getUser(Authentication authentication) {
        User user = null;

        // Check if the principal in the authentication is an instance of UserDetails
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            user = userRepository.findByEmail(userDetails.getUsername());
        } else if (authentication.getPrincipal() instanceof User) {
            // Alternatively, check if the principal is a User object directly
            user = (User) authentication.getPrincipal();
        }

        if (user != null) {
            // Build and return a UserResponseDto containing user information
            return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
        } else {
            return null;
        }
    }
}