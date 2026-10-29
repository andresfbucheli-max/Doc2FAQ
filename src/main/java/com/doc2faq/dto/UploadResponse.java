package com.doc2faq.dto;

/**
 * Response DTO returned after a file upload attempt.
 */
public class UploadResponse {

    private final boolean success;
    private final String message;
    private final String fileId;

    private UploadResponse(boolean success, String message, String fileId) {
        this.success = success;
        this.message = message;
        this.fileId = fileId;
    }

    public static UploadResponse ok(String fileId, String originalFilename) {
        return new UploadResponse(
                true,
                "File '" + originalFilename + "' uploaded successfully.",
                fileId
        );
    }

    public static UploadResponse error(String message) {
        return new UploadResponse(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getFileId() {
        return fileId;
    }
}
