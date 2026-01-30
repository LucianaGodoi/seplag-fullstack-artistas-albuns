package br.gov.mt.seplag.artists_api.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
  //  private String url;
    private String internalUrl;
    private String publicUrl;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
