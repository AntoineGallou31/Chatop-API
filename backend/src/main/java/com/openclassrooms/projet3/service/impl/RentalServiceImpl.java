package com.openclassrooms.projet3.service.impl;

import com.openclassrooms.projet3.dto.request.UpdateRentalDto;
import com.openclassrooms.projet3.dto.response.MessageResponse;
import com.openclassrooms.projet3.dto.response.RentalResponse;
import com.openclassrooms.projet3.dto.response.RentalsResponse;
import com.openclassrooms.projet3.entity.Rental;
import com.openclassrooms.projet3.entity.User;
import com.openclassrooms.projet3.exception.ResourceNotFoundException;
import com.openclassrooms.projet3.exception.UnauthorizedException;
import com.openclassrooms.projet3.repository.RentalRepository;
import com.openclassrooms.projet3.repository.UserRepository;
import com.openclassrooms.projet3.service.ImageStorageService;
import com.openclassrooms.projet3.service.RentalService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class RentalServiceImpl implements RentalService {

    @Autowired
    private final RentalRepository rentalRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserServiceImpl userServiceImpl;
    @Autowired
    private ImageStorageService imageStorageService;

    // Retrieves all rentals, maps each Rental to a RentalResponse, and returns them in a RentalsResponse object.
    @Override
    public RentalsResponse getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        List<RentalResponse> rentalResponses = rentals.stream()
                .map(this::rentalToRentalResponse)
                .collect(Collectors.toList());
        return new RentalsResponse(rentalResponses);
    }

    // Retrieves a rental by its ID and maps it to a RentalResponse.
    @Override
    public RentalResponse getRentalById(Long id) {
        Optional<Rental> rentalOptional = rentalRepository.findById(id);
        if (rentalOptional.isPresent()) {
            Rental rental = rentalOptional.get();
            return rentalToRentalResponse(rental);
        } else {
            throw new ResourceNotFoundException("Rental not found with ID : " + id);
        }
    }

    // Creates a new rental with the provided details and associates it with the authenticated user.
    public MessageResponse createRental(String name, BigDecimal surface, BigDecimal price, String description, MultipartFile pictureInput) throws IOException {

        String imageUrl = null;

        // Save the image and get its URL.
        if (pictureInput != null && !pictureInput.isEmpty()) {
            try {
                imageUrl = imageStorageService.saveImage(pictureInput);
            } catch (IOException e) {
                throw new RuntimeException("Error while picture upload", e);
            }
        }

        // Get the authenticated user's email.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("user not authenticated");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);

        // Build the Rental entity.
        Rental rental = Rental.builder()
                .name(name)
                .surface(surface)
                .price(price)
                .description(description)
                .picture(imageUrl)
                .owner(user)
                .build();

        // Save the new rental in the repository.
        rentalRepository.save(rental);

        // Return a success message.
        return new MessageResponse().setMessage("Rental created !");
    }

    // Updates an existing rental identified by its ID with the provided data.
    public MessageResponse updateRental(Long id, UpdateRentalDto updatedRentalDto) {
        Optional<Rental> rentalOptional = rentalRepository.findById(id);
        if (rentalOptional.isPresent()) {
            Rental rental = rentalOptional.get();
            // Update the rental fields.
            rental.setName(updatedRentalDto.getName());
            rental.setSurface(updatedRentalDto.getSurface());
            rental.setPrice(updatedRentalDto.getPrice());
            rental.setDescription(updatedRentalDto.getDescription());
            rentalRepository.save(rental);
            return new MessageResponse().setMessage("Rental updated !");
        } else {
            // Return a message if the rental is not found.
            return new MessageResponse().setMessage("Rental not found !");
        }
    }

    // Helper method to map a Rental entity to a RentalResponse DTO.
    private RentalResponse rentalToRentalResponse(Rental rental) {
        RentalResponse rentalResponse = new RentalResponse();
        rentalResponse.setId(rental.getId());
        rentalResponse.setName(rental.getName());
        rentalResponse.setSurface(rental.getSurface());
        rentalResponse.setPrice(rental.getPrice());
        rentalResponse.setPicture(rental.getPicture());
        rentalResponse.setDescription(rental.getDescription());
        rentalResponse.setOwner_id(rental.getOwner().getId());
        rentalResponse.setCreatedAt(rental.getCreatedAt());
        rentalResponse.setUpdatedAt(rental.getUpdatedAt());
        return rentalResponse;
    }
}