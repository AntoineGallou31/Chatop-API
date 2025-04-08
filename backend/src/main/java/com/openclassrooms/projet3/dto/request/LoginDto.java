package com.openclassrooms.projet3.dto.request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Data
public class LoginDto {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
