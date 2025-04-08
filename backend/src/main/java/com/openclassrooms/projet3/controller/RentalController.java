package com.openclassrooms.projet3.controller;

import com.openclassrooms.projet3.dto.request.UpdateRentalDto;
import com.openclassrooms.projet3.dto.response.MessageResponse;
import com.openclassrooms.projet3.dto.response.RentalResponse;
import com.openclassrooms.projet3.dto.response.RentalsResponse;
import com.openclassrooms.projet3.entity.Rental;
import com.openclassrooms.projet3.service.RentalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@Data
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "Operations related to rentals")
public class RentalController {

    @Autowired
    private final RentalService rentalService;

    // Endpoint to retrieve all rentals
    @GetMapping
    @Operation(summary = "Get all rentals", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Rental.class)))
    })
    public ResponseEntity<RentalsResponse> getAllRentals() {
        // Retrieve all rentals and wrap the result in RentalsResponse
        RentalsResponse response = rentalService.getAllRentals();
        return ResponseEntity.ok(response);
    }

    // Endpoint to retrieve a rental by its id
    @GetMapping("/{id}")
    @Operation(summary = "Get rental by id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Rental.class))),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    public ResponseEntity<RentalResponse> getRentalById(
            @Parameter(description = "Rental ID", required = true) @PathVariable Long id) {
        // Retrieve rental by id and convert it to RentalResponse
        RentalResponse rentalResponse = rentalService.getRentalById(id);
        return ResponseEntity.ok(rentalResponse);
    }

    // Endpoint to create a new rental
    @PostMapping
    @Operation(summary = "Create a new rental", responses = {
            @ApiResponse(responseCode = "200", description = "Rental created successfully", content = @Content(schema = @Schema(implementation = Rental.class))),
            @ApiResponse(responseCode = "413", description = "Payload Too Large - File size exceeded limit.")
    })
    public MessageResponse createRental(
            // Rental parameters from request
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile file) throws IOException {

        // Call service layer to create a new rental
        return this.rentalService.createRental(name, surface, price, description, file);
    }

    // Exception handler for multipart exceptions (e.g., file size too large)
    @ExceptionHandler(MultipartException.class)
    @Operation(hidden = true) // Hide this endpoint from Swagger UI
    public ResponseEntity<String> handleMultipartException(MultipartException e) {
        // Return error response indicating file size limit exceeded
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeded limit.");
    }

    // Endpoint to update an existing rental
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update a rental", responses = {
            @ApiResponse(responseCode = "200", description = "Rental updated successfully", content = @Content(schema = @Schema(implementation = Rental.class))),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    public MessageResponse updateRental(
            // Rental id and updated data
            @Parameter(description = "Rental ID to update", required = true) @PathVariable Long id,
            @Parameter(description = "Rental object containing updated data", required = true) @ModelAttribute UpdateRentalDto updateRentalDto) {

        // Call service layer to update the rental entity
        return this.rentalService.updateRental(id, updateRentalDto);
    }
}