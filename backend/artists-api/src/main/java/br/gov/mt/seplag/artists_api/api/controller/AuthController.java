package br.gov.mt.seplag.artists_api.api.controller;

import br.gov.mt.seplag.artists_api.api.dto.*;
import br.gov.mt.seplag.artists_api.domain.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager
    ) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO req
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.username(),
                        req.password()
                )
        );

        var tokens = authService.login(req.username(), req.password());
        return ResponseEntity.ok(
                new AuthResponseDTO(
                        tokens.accessToken(),
                        tokens.refreshToken(),
                        "Bearer"
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(
            @Valid @RequestBody RefreshRequestDTO req
    ) {
        var tokens = authService.refresh(req.refreshToken());
        return ResponseEntity.ok(
                new AuthResponseDTO(
                        tokens.accessToken(),
                        tokens.refreshToken(),
                        "Bearer"
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshRequestDTO req
    ) {
        authService.logout(req.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
