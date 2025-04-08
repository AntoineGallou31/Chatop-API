package com.openclassrooms.projet3.service.impl;

import com.openclassrooms.projet3.service.ImageStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    // Directory where images will be uploaded
    private final String uploadDir = "uploads/";

    /**
     * Saves the provided image file and returns the URL to access the image.
     *
     * @param file the image file to save
     * @return the URL of the uploaded image
     */
    public String saveImage(MultipartFile file) throws IOException {
        // Generate a unique file name by combining a random UUID with the original filename
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Create the file path using the upload directory and the generated file name
        Path filePath = Paths.get(uploadDir, fileName);

        // Create directories if they do not exist
        Files.createDirectories(filePath.getParent());

        // Write the file bytes to the specified path
        Files.write(filePath, file.getBytes());

        // Returns the URL to access the uploaded image
        return "http://localhost:3001/api/images/" + fileName;
    }
}