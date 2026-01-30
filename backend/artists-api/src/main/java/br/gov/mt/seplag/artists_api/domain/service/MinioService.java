package br.gov.mt.seplag.artists_api.domain.service;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.public-url}")
    private String publicUrl;

    public String gerarUrlPublica(String objectKey) {
        return String.format(
                "%s/%s/%s",
                publicUrl.replaceAll("/$", ""),
                bucket,
                objectKey
        );
    }
}
