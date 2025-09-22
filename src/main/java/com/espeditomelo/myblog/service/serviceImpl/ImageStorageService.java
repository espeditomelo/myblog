package com.espeditomelo.myblog.service.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final Path rootLocation;

    public ImageStorageService() {
        this.rootLocation = Paths.get("./images");
        init();
    }

    private void init() {
        try {
            if(!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize image storage", e);
        }
    }

    public String store(MultipartFile multipartFile) {
        try {
            if(multipartFile.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }

            String originalFilename = multipartFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;
            Path destinationFile = this.rootLocation.resolve(Paths.get(newFilename)).normalize().toAbsolutePath();
            Files.copy(multipartFile.getInputStream(), destinationFile);
            return "/images/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

}
