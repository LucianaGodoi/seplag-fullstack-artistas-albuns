package br.gov.mt.seplag.artists_api.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class RateLimitFilter extends OncePerRequestFilter {

    private final ConcurrentMap<String, Bucket> buckets;
    private final int requestsPerMinute;
    private final Set<String> excludedPrefixes;

    public RateLimitFilter(
            ConcurrentMap<String, Bucket> buckets,
            int requestsPerMinute,
            Set<String> excludedPrefixes
    ) {
        this.buckets = buckets;
        this.requestsPerMinute = requestsPerMinute;
        this.excludedPrefixes = excludedPrefixes;
    }

    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.classic(
                requestsPerMinute,
                Refill.intervally(requestsPerMinute, Duration.ofMinutes(1))
        );
        return Bucket.builder().addLimit(limit).build();
    }

    private String resolveKey(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
            return "user:" + auth.getName();
        }
        return "ip:" + request.getRemoteAddr();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/ws")) {
            chain.doFilter(request, response);
            return;
        }

        for (String prefix : excludedPrefixes) {
            if (path.startsWith(prefix)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String key = resolveKey(request);
        Bucket bucket = buckets.computeIfAbsent(key, k -> newBucket());

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.getWriter().write("Rate limit exceeded (" + requestsPerMinute + " req/min).");
    }
}
