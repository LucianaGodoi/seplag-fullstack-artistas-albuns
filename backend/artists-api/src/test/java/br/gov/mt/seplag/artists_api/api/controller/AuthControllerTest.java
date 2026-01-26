package br.gov.mt.seplag.artists_api.api.controller;

import br.gov.mt.seplag.artists_api.domain.service.AuthService;
import br.gov.mt.seplag.artists_api.domain.service.AuthService.AuthTokens;
import br.gov.mt.seplag.artists_api.domain.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;


    // ================= LOGIN =================

    @Test
    void deveRealizarLoginComSucesso() throws Exception {

        Mockito.when(authService.login("admin", "123"))
                .thenReturn(new AuthTokens("access-token", "refresh-token"));

        String body = """
            {
              "username": "admin",
              "password": "123"
            }
            """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    // ================= REFRESH =================

    @Test
    void deveRealizarRefreshComSucesso() throws Exception {

        Mockito.when(authService.refresh("refresh-token"))
                .thenReturn(new AuthTokens("novo-access", "novo-refresh"));

        String body = """
            {
              "refreshToken": "refresh-token"
            }
            """;

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("novo-access"))
                .andExpect(jsonPath("$.refreshToken").value("novo-refresh"));
    }

}
