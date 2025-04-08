package com.openclassrooms.projet3.dto.request;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateRentalDto {
    private String name;

    private double surface;

    private double price;

    private MultipartFile picture;

    private String description;
}
