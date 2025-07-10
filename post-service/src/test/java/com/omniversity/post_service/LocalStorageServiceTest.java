package com.omniversity.post_service;

import com.omniversity.post_service.exception.custom.storage.*;
import com.omniversity.post_service.service.storage.LocalStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocalStorageServiceTest {

    private LocalStorageService localStorageService;

    @BeforeEach
    void setUp() throws StorageInitializationException, IOException {
        Path uploadsDir = Paths.get("uploads");
        if (Files.exists(uploadsDir)) {
            Files.walk(uploadsDir)
                 .sorted(java.util.Comparator.reverseOrder())
                 .map(Path::toFile)
                 .forEach(java.io.File::delete);
        }
        localStorageService = new LocalStorageService();
    }

    

    @Test
    void store_Success() throws StorageException, IOException {
        String fileName = "test.txt";
        String content = "Hello, world!";
        MockMultipartFile file = new MockMultipartFile("file", fileName, "text/plain", content.getBytes());

        String storedPath = localStorageService.store(file);

        Path expectedPath = Paths.get("uploads").resolve(fileName);
        assertEquals(expectedPath.toString(), storedPath);
        assertTrue(Files.exists(expectedPath));
        assertEquals(content, Files.readString(expectedPath));
    }

    @Test
    void store_InvalidFileException_NullFile() {
        InvalidFileException exception = assertThrows(InvalidFileException.class, () -> localStorageService.store(null));
        assertEquals("Cannot store empty file.", exception.getMessage());
    }

    @Test
    void store_InvalidFileException_EmptyFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]);
        InvalidFileException exception = assertThrows(InvalidFileException.class, () -> localStorageService.store(file));
        assertEquals("Cannot store empty file.", exception.getMessage());
    }

    @Test
    void store_InvalidFileNameException_NullFileName() {
        MockMultipartFile file = new MockMultipartFile("file", null, "text/plain", "content".getBytes());
        InvalidFileNameException exception = assertThrows(InvalidFileNameException.class, () -> localStorageService.store(file));
        assertEquals("File name cannot be empty.", exception.getMessage());
    }

    @Test
    void store_InvalidFileNameException_EmptyFileName() {
        MockMultipartFile file = new MockMultipartFile("file", "  ", "text/plain", "content".getBytes());
        InvalidFileNameException exception = assertThrows(InvalidFileNameException.class, () -> localStorageService.store(file));
        assertEquals("File name cannot be empty.", exception.getMessage());
    }

    @Test
    void store_InvalidFileNameException_PathTraversal() {
        MockMultipartFile file = new MockMultipartFile("file", "../test.txt", "text/plain", "content".getBytes());
        InvalidFileNameException exception = assertThrows(InvalidFileNameException.class, () -> localStorageService.store(file));
        assertEquals("File name contains invalid path characters.", exception.getMessage());
    }

    @Test
    void store_FileStorageException_IOException() throws IOException {
        MockMultipartFile mockFile = mock(MockMultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("error.txt");
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getInputStream()).thenThrow(new IOException("Simulated IO error"));

        FileStorageException exception = assertThrows(FileStorageException.class, () -> localStorageService.store(mockFile));
        assertTrue(exception.getMessage().contains("Failed to store file"));
        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    void delete_Success() throws StorageException, IOException {
        String fileName = "to_delete.txt";
        Path filePath = Paths.get("uploads").resolve(fileName);
        Files.createFile(filePath);

        localStorageService.delete(filePath.toString());

        assertFalse(Files.exists(filePath));
    }

    @Test
    void delete_IllegalFilePathException_NullPath() {
        IllegalFilePathException exception = assertThrows(IllegalFilePathException.class, () -> localStorageService.delete(null));
        assertEquals("File path cannot be empty.", exception.getMessage());
    }

    @Test
    void delete_IllegalFilePathException_EmptyPath() {
        InvalidFileNameException exception = assertThrows(InvalidFileNameException.class, () -> localStorageService.delete("  "));
        assertTrue(exception.getMessage().contains("Invalid file path provided for deletion"));
    }

    @Test
    void delete_InvalidFileNameException_InvalidPath() {
        InvalidFileNameException exception = assertThrows(InvalidFileNameException.class, () -> localStorageService.delete("invalid\0path"));
        assertTrue(exception.getMessage().contains("Invalid file path provided for deletion"));
    }


    @Test
    void delete_FileDeletionException_NoSuchFileException() {
        FileDeletionException exception = assertThrows(FileDeletionException.class, () -> localStorageService.delete(Paths.get("uploads").resolve("non_existent.txt").toString()));
        assertTrue(exception.getMessage().contains("File not found for deletion"));
    }

    @Test
    void delete_FileDeletionException_DirectoryNotEmptyException() throws IOException {
        String dirName = "non_empty_dir";
        Path dirPath = Paths.get("uploads").resolve(dirName);
        Files.createDirectory(dirPath);
        Files.createFile(dirPath.resolve("file.txt"));

        FileDeletionException exception = assertThrows(FileDeletionException.class, () -> localStorageService.delete(dirPath.toString()));
        assertTrue(exception.getMessage().contains("Cannot delete directory because it is not empty"));
        assertTrue(Files.exists(dirPath)); // Should not be deleted
    }

    

    
}