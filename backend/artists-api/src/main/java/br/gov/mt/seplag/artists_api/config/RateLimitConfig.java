package br.gov.mt.seplag.artists_api.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class RateLimitConfig {

    @Bean
    public ConcurrentMap<String, Bucket> rateLimitBuckets() {
        return Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(30))
                .maximumSize(50_000)
                .<String, Bucket>build() //força o cache ser do tipo certo
                .asMap();
    }

    @Bean
    public RateLimitFilter rateLimitFilter(ConcurrentMap<String, Bucket> rateLimitBuckets) {
        var excluded = Set.of(
                "/api/v1/auth",
                "/v3/api-docs",
                "/swagger-ui",
                "/swagger-ui.html",
                "/actuator"
        );

        return new RateLimitFilter(rateLimitBuckets, 10, excluded);
    }
}
