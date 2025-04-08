package com.openclassrooms.projet3.service;

import com.openclassrooms.projet3.dto.response.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDetails loadUserByEmail(String email);

    UserResponseDto getUserById(Long id);
}
