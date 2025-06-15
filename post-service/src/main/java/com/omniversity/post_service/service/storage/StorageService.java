package com.omniversity.post_service.service.storage;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file) throws IOException;
    void delete(String filePath) throws IOException;
}
