package br.gov.mt.seplag.artists_api.domain.service;

import br.gov.mt.seplag.artists_api.config.JwtProperties;
import br.gov.mt.seplag.artists_api.domain.entity.RefreshToken;
import br.gov.mt.seplag.artists_api.domain.entity.Usuario;
import br.gov.mt.seplag.artists_api.domain.repository.RefreshTokenRepository;
import br.gov.mt.seplag.artists_api.domain.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties props;

    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(
            UsuarioRepository usuarioRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            JwtProperties props
    ) {
        this.usuarioRepository = usuarioRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.props = props;
    }

    public AuthTokens login(String username, String password) {
        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!Boolean.TRUE.equals(u.getAtivo())) {
            throw new BadCredentialsException("Usuário inativo");
        }

        if (!passwordEncoder.matches(password, u.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String access = jwtService.generateAccessToken(u.getUsername(), u.getRole());
        String refreshPlain = generateOpaqueToken();
        String refreshHash = sha256Hex(refreshPlain);

        RefreshToken rt = RefreshToken.builder()
                .usuario(u)
                .tokenHash(refreshHash)
                .expiresAt(LocalDateTime.now().plusDays(props.getRefreshTokenDays()))
                .revokedAt(null)
                .build();

        refreshTokenRepository.save(rt);

        return new AuthTokens(access, refreshPlain);
    }

    /**
     * Rotação: invalida o refresh antigo e emite um novo.
     */
    public AuthTokens refresh(String refreshTokenPlain) {
        String hash = sha256Hex(refreshTokenPlain);

        RefreshToken rt = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new BadCredentialsException("Refresh token inválido"));

        if (rt.isRevoked() || rt.isExpired()) {
            throw new BadCredentialsException("Refresh token expirado/revogado");
        }

        // revoga o atual
        rt.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(rt);

        Usuario u = rt.getUsuario();
        if (!Boolean.TRUE.equals(u.getAtivo())) {
            throw new BadCredentialsException("Usuário inativo");
        }

        // emite novo par
        String access = jwtService.generateAccessToken(u.getUsername(), u.getRole());
        String newRefreshPlain = generateOpaqueToken();
        String newRefreshHash = sha256Hex(newRefreshPlain);

        RefreshToken newRt = RefreshToken.builder()
                .usuario(u)
                .tokenHash(newRefreshHash)
                .expiresAt(LocalDateTime.now().plusDays(props.getRefreshTokenDays()))
                .build();

        refreshTokenRepository.save(newRt);

        return new AuthTokens(access, newRefreshPlain);
    }

    public void logout(String refreshTokenPlain) {
        String hash = sha256Hex(refreshTokenPlain);

        refreshTokenRepository.findByTokenHash(hash).ifPresent(rt -> {
            if (!rt.isRevoked()) {
                rt.setRevokedAt(LocalDateTime.now());
                refreshTokenRepository.save(rt);
            }
        });
    }

    private String generateOpaqueToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private String sha256Hex(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(value.getBytes());
            return HexFormat.of().formatHex(dig);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar hash", e);
        }
    }

    public record AuthTokens(String accessToken, String refreshToken) {}
}
