package com.example.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class StorageService {
    private final S3Client s3Client;
    private final String bucketName;

    @Autowired
    public StorageService(S3Client s3Client, @Value("${aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file, String oldFileUrl) {
        try {
            // สร้างชื่อไฟล์ที่ไม่ซ้ำ
            String fileName = "profiles/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            // อัปโหลดไฟล์ไป S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            // ลบไฟล์เก่าถ้ามี
            if (oldFileUrl != null && !oldFileUrl.isEmpty()) {
                String oldFileKey = extractKeyFromUrl(oldFileUrl);
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(oldFileKey)
                        .build();
                s3Client.deleteObject(deleteObjectRequest);
            }

            // ส่งคืน URL ของไฟล์
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    private String extractKeyFromUrl(String url) {
        try {
            java.net.URI uri = new java.net.URI(url);
            String path = uri.getPath(); // เช่น "/profiles/xxx.png"
            if (path.startsWith("/")) path = path.substring(1);
            return path;
        } catch (Exception e) {
            throw new RuntimeException("Invalid URL: " + url, e);
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) return;

        try {
            String key = extractKeyFromUrl(fileUrl);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);

            System.out.println("Deleted file from S3: " + key); // log ไว้เช็ค
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }
}
