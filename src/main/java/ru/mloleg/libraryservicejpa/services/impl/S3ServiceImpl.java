package ru.mloleg.libraryservicejpa.services.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mloleg.libraryservicejpa.exception.ImageUploadException;
import ru.mloleg.libraryservicejpa.services.S3Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 s3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public S3ServiceImpl(AmazonS3 s3) {
        this.s3 = s3;
    }

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("Image must have name.");
        }

        String fileName = generateFileName(file);

        try (InputStream inputStream = file.getInputStream()) {
            s3.putObject(bucketName, fileName, inputStream, null);
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed" + e.getMessage());
        }

        return fileName;
    }

    public void deleteFile(String keyName) {
        s3.deleteObject(new DeleteObjectRequest(bucketName, keyName));
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() + file.getOriginalFilename();
    }
}
