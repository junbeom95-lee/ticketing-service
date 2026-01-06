package com.chil.ticketingservice.domain.show.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.base-folder:shows}")
    private String baseFolder;

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required.");
        }

        String ext = getExt(file.getOriginalFilename());
        String key = baseFolder + "/" + UUID.randomUUID() + ext;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream is = file.getInputStream()) {
            amazonS3.putObject(bucket, key, is, metadata);
        } catch (Exception e) {
            throw new RuntimeException("S3 upload failed", e);
        }

        return amazonS3.getUrl(bucket, key).toString();
    }

    public void deleteByUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return;

        String key = extractKeyFromUrl(imageUrl);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
    }

    private String extractKeyFromUrl(String url) {
        try {
            URI uri = URI.create(url);
            String path = uri.getPath();
            if (path == null) throw new IllegalArgumentException("Invalid S3 url path: " + url);

            path = path.startsWith("/") ? path.substring(1) : path;

            if (path.startsWith(bucket + "/")) {
                return path.substring((bucket + "/").length());
            }
            return path;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to extract S3 key from url: " + url, e);
        }
    }

    private String getExt(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return (dot >= 0) ? filename.substring(dot) : "";
    }
}
