package com.openclassrooms.projet3.service.impl;

import com.openclassrooms.projet3.dto.response.UserResponseDto;
import com.openclassrooms.projet3.entity.User;
import com.openclassrooms.projet3.repository.UserRepository;
import com.openclassrooms.projet3.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    /**
     * Loads a user by their email.
     *
     * @param email the user's email, used as the username.
     * @return a UserDetails object containing user information and roles.
     * @throws UsernameNotFoundException if no user is found with the provided email.
     */
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        // Search for the user in the database using the email
        User user = userRepository.findByEmail(email);

        // Return a Spring Security User object with username, password, and authorities
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities() // Ensure getAuthorities() returns a valid GrantedAuthority list
        );
    }

    /**
     * Retrieves a user by their ID and converts it to a UserResponseDto.
     *
     * @param id the user's identifier.
     * @return a UserResponseDto object containing user details.
     * @throws UsernameNotFoundException if no user is found with the provided ID.
     */
    public UserResponseDto getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return userToUserResponseDto(user);
        } else {
            // Throw an exception if the user is not found
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
    }

    /**
     * Converts a User entity to a UserResponseDto.
     *
     * @param user the User entity to convert.
     * @return a UserResponseDto populated with user information.
     */
    private UserResponseDto userToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        // Optionally, set createdAt and updatedAt if needed
        return userResponseDto;
    }
}