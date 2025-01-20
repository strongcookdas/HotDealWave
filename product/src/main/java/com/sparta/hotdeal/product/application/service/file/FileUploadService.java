package com.sparta.hotdeal.product.application.service.file;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class FileUploadService {
    @Value("${s3.bucket-name}")
    private String bucketName;

    @Value("${s3.region}")
    private String region;

    @Value("${s3.access-key}")
    private String accessKeyId;

    @Value("${s3.secret-key}")
    private String secretAccessKey;

    public void uploadFile(MultipartFile file, String path, String filename) throws IOException {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                accessKeyId,
                secretAccessKey);

        S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds)) // 하드코딩된 자격 증명 사용
                .build();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(path + "/" + filename)
                .build();

        s3Client.putObject(putObjectRequest,
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
    }

    public void deleteFile(String fileKey) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                accessKeyId,
                secretAccessKey);

        S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}
