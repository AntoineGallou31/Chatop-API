// Java
package com.openclassrooms.projet3.controller;

import com.openclassrooms.projet3.dto.response.UserResponseDto;
import com.openclassrooms.projet3.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/api/user")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {

    @Autowired
    private final UserService userService;

    // Endpoint to retrieve a user by their ID
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponseDto getUserById(
        @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        return this.userService.getUserById(id);
    }
}