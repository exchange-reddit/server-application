package com.omniversity.post_service.service.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("local")
public class LocalStorageService implements StorageService {

    // Typically, the CWD is set to the root directory of your project.
    // So, if your project is at /Users/yourusername/Documents/MyJavaProject,
    // then uploads would resolve to /Users/yourusername/Documents/MyJavaProject/uploads.
    private final Path root = Paths.get("uploads");

    public LocalStorageService() throws IOException {
        Files.createDirectories(root);
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        Path destination = root.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return destination.toString();
    }

    @Override
    public void delete(String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            return; // Nothing to delete
        }
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
}
