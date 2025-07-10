package com.omniversity.post_service.service.storage;

import java.io.IOException;

import com.omniversity.post_service.exception.custom.storage.FileDeletionException;
import com.omniversity.post_service.exception.custom.storage.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface StorageService {
    String store(MultipartFile file) throws StorageException;
    void delete(String filePath) throws StorageException;
}
