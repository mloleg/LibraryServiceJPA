package ru.mloleg.libraryservicejpa.services;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadFile(MultipartFile file);

    void deleteFile(String keyName);
}
