package com.doc2faq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Creates and configures the AWS S3 client bean.
 * Credentials are resolved automatically via the default AWS credential chain
 * (environment variables, IAM role, ~/.aws/credentials, etc.).
 */
@Configuration
public class S3Config {

    @Value("${app.s3.region}")
    private String region;

    /**
     * S3Client uses the default credential provider chain, which supports
     * environment variables (AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY),
     * IAM instance profiles, and AWS profiles – no credentials are hard-coded.
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .build();
    }
}
