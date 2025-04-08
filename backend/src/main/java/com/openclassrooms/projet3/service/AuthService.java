package com.openclassrooms.projet3.service;

import com.openclassrooms.projet3.dto.request.LoginDto;
import com.openclassrooms.projet3.dto.request.RegisterUserDto;
import com.openclassrooms.projet3.dto.response.JwtResponse;
import com.openclassrooms.projet3.dto.response.UserResponseDto;
import org.springframework.security.core.Authentication;

public interface AuthService {

    JwtResponse register(RegisterUserDto userDto);

    JwtResponse authenticate(LoginDto loginDto);

    UserResponseDto getUser(Authentication authentication);
}
