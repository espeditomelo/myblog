package com.espeditomelo.myblog.service.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final Path rootLocation;

    public ImageStorageService() {
//        this.rootLocation = Paths.get("./images");
        this.rootLocation = Paths.get("images");
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

            System.out.println(">>>>>>>>>>>>>>>>>> destinationFile.getParent(): " + destinationFile.getParent());
            System.out.println(">>>>>>>>>>>>>>>>>> this.rootLocation.toAbsolutePath(): " + this.rootLocation.toAbsolutePath());

            Path rootPath = this.rootLocation.toAbsolutePath().normalize();
            if (!destinationFile.getParent().equals(rootPath)) {
                throw new RuntimeException("Can't store file outside current directory");
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/images/" + newFilename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    public void delete(String imageUrl) {
        try {
            if (imageUrl != null && imageUrl.startsWith("/images/")) {
                String filename = imageUrl.substring("/images/".length());
                Path filePath = this.rootLocation.resolve(filename).normalize().toAbsolutePath();

                if (filePath.getParent().equals(this.rootLocation.toAbsolutePath())) {
                    Files.deleteIfExists(filePath);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to delete image: " + imageUrl);
        }
    }

}
