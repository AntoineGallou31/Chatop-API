package com.openclassrooms.projet3.service;

import com.openclassrooms.projet3.dto.request.UpdateRentalDto;
import com.openclassrooms.projet3.dto.response.MessageResponse;
import com.openclassrooms.projet3.dto.response.RentalResponse;
import com.openclassrooms.projet3.dto.response.RentalsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;


public interface RentalService {
    RentalsResponse getAllRentals();

    RentalResponse getRentalById(Long id);

    MessageResponse createRental(String name, BigDecimal surface, BigDecimal price, String description, MultipartFile pictureInput) throws IOException;

    MessageResponse updateRental(Long id, UpdateRentalDto updatedRentalDto);
}
