package br.gov.mt.seplag.artists_api.domain.service;

import br.gov.mt.seplag.artists_api.config.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@Service
public class StorageService {

    private final MinioClient minio;
    private final MinioProperties props;

    public StorageService(MinioClient minio, MinioProperties props) {
        this.minio = minio;
        this.props = props;
    }

    public String uploadAlbumCover(Long albumId, String originalFilename, String contentType, InputStream inputStream, long size) {
        String safeName = (originalFilename == null || originalFilename.isBlank()) ? "file" : originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        String objectKey = "albums/%d/%s_%s".formatted(albumId, UUID.randomUUID(), safeName);

        try {
            minio.putObject(
                    PutObjectArgs.builder()
                            .bucket(props.getBucket())
                            .object(objectKey)
                            .stream(inputStream, size, -1)
                            .contentType(contentType != null ? contentType : "application/octet-stream")
                            .build()
            );
            return objectKey;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar arquivo para MinIO", e);
        }
    }

    public String presignedGetUrl(String objectKey, Duration expiry) {
        try {
            int seconds = (int) Math.min(expiry.getSeconds(), 60L * 60L); // hard cap 1h
            return minio.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(props.getBucket())
                            .object(objectKey)
                            .expiry(seconds)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar presigned URL", e);
        }
    }
}
