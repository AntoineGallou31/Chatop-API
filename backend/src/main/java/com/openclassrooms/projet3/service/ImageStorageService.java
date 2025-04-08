package com.openclassrooms.projet3.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {
    String saveImage(MultipartFile file) throws IOException;
}
