package com.doc2faq.controller;

import com.doc2faq.dto.UploadResponse;
import com.doc2faq.service.FileUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Controller
public class HomeController {

    private final FileUploadService fileUploadService;

    public HomeController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("appName", "Doc2FAQ");
        model.addAttribute("appDescription",
                "Transform your documents into structured FAQs automatically.");
        return "index";
    }

    /**
     * Accepts a multipart file upload, validates it, stores it in S3,
     * and returns a JSON response with the result and unique file ID.
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<UploadResponse> upload(
            @RequestParam("file") MultipartFile file) {

        UploadResponse response = fileUploadService.upload(file);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Handles the case where Spring rejects the multipart request because the
     * file exceeds the configured max size before it even reaches the controller.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity<UploadResponse> handleMaxSizeExceeded(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UploadResponse.error(
                        "File is too large. Maximum allowed size is 10 MB."));
    }

}
