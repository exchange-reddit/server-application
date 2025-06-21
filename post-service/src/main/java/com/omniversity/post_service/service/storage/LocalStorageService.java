package com.omniversity.post_service.service.storage;

import java.io.IOException;
import java.nio.file.*;

import com.omniversity.post_service.exception.custom.storage.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("local")
public class LocalStorageService implements StorageService {

    // Typically, the CWD is set to the root directory of your project.
    // So, if your project is at /Users/yourusername/Documents/MyJavaProject,
    // then uploads would resolve to /Users/yourusername/Documents/MyJavaProject/uploads.
    private final Path root = Paths.get("uploads");

    public LocalStorageService() throws StorageInitializationException { // Custom exception
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new StorageInitializationException("Failed to create storage directory: " + root.toAbsolutePath(), e);
        }
    }

    @Override
    public String store(MultipartFile file) throws StorageException {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("Cannot store empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new InvalidFileNameException("File name cannot be empty.");
        }

        // Sanitize filename to prevent path traversal issues
        String cleanFilename = StringUtils.cleanPath(originalFilename); // Use Spring's StringUtils or custom logic
        if (cleanFilename.contains("..") || cleanFilename.contains("/") || cleanFilename.contains("\\")) {
            throw new InvalidFileNameException("File name contains invalid path characters.");
        }

        try {
            Path destination = this.root.resolve(cleanFilename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toString();
        } catch (FileAlreadyExistsException e) { // If destination is a directory, not a file
                throw new FileStorageException("Cannot store file; a directory exists with the same name: " + cleanFilename, e);
        } catch (AccessDeniedException e) {
            // If there is insufficient permissions to write to the root directory or create the file
            throw new FileStorageException("Permission denied to store file: " + cleanFilename, e);
        } catch (IOException e) {
            // Other IOExceptions
            throw new FileStorageException("Failed to store file: " + cleanFilename, e);
        }
    }

    @Override
    public void delete(String filePath) throws StorageException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalFilePathException("File path cannot be empty.");
        }
        Path path;
        try {
            path = Paths.get(filePath);
            // Validate if the path is within the allowed root directory to prevent arbitrary file deletion
            if (!path.normalize().startsWith(this.root.normalize())) {
                throw new FileDeletionException("Attempt to delete file outside of allowed storage directory: " + filePath);
            }
        } catch (InvalidPathException e) {
            throw new InvalidFileNameException("Invalid file path provided for deletion: " + filePath, e);
        }

        try {
            // If you want it to be a no-op when file doesn't exist, keep the if check or catch NoSuchFileException
            Files.delete(path);
        } catch (NoSuchFileException e) {
            // This means the file wasn't found. Depending on your logic, you might:
            // 1. Log it as a warning (if deletion of non-existent is common/acceptable)
            // 2. Throw a more specific exception like FileNotFoundError (if it's an error for your app)
            System.out.println("Warning: Attempted to delete non-existent file: " + filePath);
            throw new FileDeletionException("File not found for deletion: " + filePath, e);
        } catch (DirectoryNotEmptyException e) {
            throw new FileDeletionException("Cannot delete directory because it is not empty: " + filePath, e);
        } catch (AccessDeniedException e) {
            throw new FileDeletionException("Permission denied to delete file: " + filePath, e);
        } catch (IOException e) {
            throw new FileDeletionException("Failed to delete file: " + filePath, e);
        }

    }
}
