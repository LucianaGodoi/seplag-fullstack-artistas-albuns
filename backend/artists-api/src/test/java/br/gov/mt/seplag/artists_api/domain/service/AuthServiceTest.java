package br.gov.mt.seplag.artists_api.domain.service;

import br.gov.mt.seplag.artists_api.config.JwtProperties;
import br.gov.mt.seplag.artists_api.domain.entity.RefreshToken;
import br.gov.mt.seplag.artists_api.domain.entity.Usuario;
import br.gov.mt.seplag.artists_api.domain.repository.RefreshTokenRepository;
import br.gov.mt.seplag.artists_api.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = Usuario.builder()
                .id(1L)
                .username("admin")
                .password("senhaCriptografada")
                .role("ADMIN")
                .ativo(true)
                .build();
    }

    // ================= LOGIN ===================

    @Test
    void deveRealizarLoginComSucesso() {

        when(jwtProperties.getRefreshTokenDays()).thenReturn(7);

        when(usuarioRepository.findByUsername("admin"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("123", "senhaCriptografada"))
                .thenReturn(true);

        when(jwtService.generateAccessToken("admin", "ADMIN"))
                .thenReturn("access-token");

        AuthService.AuthTokens tokens =
                authService.login("admin", "123");

        assertNotNull(tokens);
        assertEquals("access-token", tokens.accessToken());
        assertNotNull(tokens.refreshToken());

        verify(refreshTokenRepository).save(any());
    }

    @Test
    void deveFalharQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findByUsername("x"))
                .thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class,
                () -> authService.login("x", "123"));
    }

    @Test
    void deveFalharQuandoUsuarioInativo() {
        usuario.setAtivo(false);

        when(usuarioRepository.findByUsername("admin"))
                .thenReturn(Optional.of(usuario));

        assertThrows(BadCredentialsException.class,
                () -> authService.login("admin", "123"));
    }

    @Test
    void deveFalharQuandoSenhaIncorreta() {
        when(usuarioRepository.findByUsername("admin"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.login("admin", "123"));
    }

    // ================= REFRESH ===================

    @Test
    void deveGerarNovoParDeTokensComRefreshValido() {

        when(jwtProperties.getRefreshTokenDays()).thenReturn(7);

        RefreshToken rt = RefreshToken.builder()
                .usuario(usuario)
                .tokenHash("hash")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();

        when(refreshTokenRepository.findByTokenHash(any()))
                .thenReturn(Optional.of(rt));

        when(jwtService.generateAccessToken("admin", "ADMIN"))
                .thenReturn("novo-access");

        AuthService.AuthTokens tokens =
                authService.refresh("refresh-token");

        assertEquals("novo-access", tokens.accessToken());
        assertNotNull(tokens.refreshToken());

        verify(refreshTokenRepository, times(2)).save(any());
    }

    @Test
    void deveFalharQuandoRefreshNaoExiste() {
        when(refreshTokenRepository.findByTokenHash(any()))
                .thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class,
                () -> authService.refresh("x"));
    }

    // ================= LOGOUT ===================

    @Test
    void deveRevogarRefreshTokenNoLogout() {
        RefreshToken rt = RefreshToken.builder()
                .usuario(usuario)
                .tokenHash("hash")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();

        when(refreshTokenRepository.findByTokenHash(any()))
                .thenReturn(Optional.of(rt));

        authService.logout("refresh");

        assertNotNull(rt.getRevokedAt());
        verify(refreshTokenRepository).save(rt);
    }
}
