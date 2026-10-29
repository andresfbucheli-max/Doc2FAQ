package com.doc2faq.service;

import com.doc2faq.dto.UploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Handles validation and upload of documents to Amazon S3.
 */
@Service
public class FileUploadService {

    private static final long MAX_SIZE_BYTES = 10L * 1024 * 1024; // 10 MB

    private static final List<String> ALLOWED_EXTENSIONS =
            Arrays.asList("pdf", "docx", "txt");

    private final S3Client s3Client;

    @Value("${app.s3.bucket-name}")
    private String bucketName;

    public FileUploadService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Validates and uploads the provided file to S3.
     *
     * @param file the multipart file received from the HTTP request
     * @return an {@link UploadResponse} containing the result and, on success,
     *         the unique file ID assigned to the uploaded object
     */
    public UploadResponse upload(MultipartFile file) {

        // Guard: empty upload
        if (file == null || file.isEmpty()) {
            return UploadResponse.error("No file was provided. Please select a file to upload.");
        }

        String originalFilename = sanitizeFilename(file.getOriginalFilename());

        // Validate extension
        String extension = getExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return UploadResponse.error(
                    "Invalid file type '." + extension + "'. "
                    + "Only .pdf, .docx, and .txt files are accepted.");
        }

        // Validate size
        if (file.getSize() > MAX_SIZE_BYTES) {
            return UploadResponse.error(
                    "File is too large (" + toMB(file.getSize()) + " MB). "
                    + "Maximum allowed size is 10 MB.");
        }

        // Generate a unique ID for this upload
        String fileId = UUID.randomUUID().toString();
        String s3Key = fileId + "/" + originalFilename;

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(resolveContentType(extension))
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        } catch (IOException e) {
            return UploadResponse.error("Failed to read the uploaded file. Please try again.");
        } catch (Exception e) {
            return UploadResponse.error("Upload failed due to a server error. Please try again.");
        }

        return UploadResponse.ok(fileId, originalFilename);
    }

    // ---- helpers -------------------------------------------------------

    private String sanitizeFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            return "unknown";
        }
        // Strip any path components to prevent path traversal
        return filename.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return "";
        }
        return filename.substring(dot + 1).toLowerCase();
    }

    private String resolveContentType(String extension) {
        return switch (extension) {
            case "pdf"  -> "application/pdf";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "txt"  -> "text/plain";
            default     -> "application/octet-stream";
        };
    }

    private String toMB(long bytes) {
        return String.format("%.1f", bytes / (1024.0 * 1024.0));
    }
}
