package com.openclassrooms.projet3.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMessageDto {
    @NotNull
    private String message;

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    @JsonProperty("rental_id")
    private Long rentalId;
}
