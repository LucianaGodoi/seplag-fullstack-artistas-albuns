package br.gov.mt.seplag.artists_api.domain.service;

import br.gov.mt.seplag.artists_api.api.dto.ArtistaRequestDTO;
import br.gov.mt.seplag.artists_api.api.dto.ArtistaResponseDTO;
import br.gov.mt.seplag.artists_api.api.dto.ArtistaResumoDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Artista;
import br.gov.mt.seplag.artists_api.domain.repository.ArtistaRepository;
import br.gov.mt.seplag.artists_api.mapper.ArtistaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistaServiceTest {

    @Mock
    private ArtistaRepository artistaRepository;

    @Mock
    private ArtistaMapper artistaMapper;

    @InjectMocks
    private ArtistaService artistaService;

    private Artista artista;
    private ArtistaRequestDTO requestDTO;
    private ArtistaResponseDTO responseDTO;
    private ArtistaResumoDTO resumoDTO;

    @BeforeEach
    void setup() {
        artista = Artista.builder()
                .id(1L)
                .nome("Artista Teste")
                .build();

        requestDTO = new ArtistaRequestDTO();
        requestDTO.setNome("Artista Teste");

        responseDTO = new ArtistaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNome("Artista Teste");

        resumoDTO = new ArtistaResumoDTO();
        resumoDTO.setId(1L);
        resumoDTO.setNome("Artista Teste");
        resumoDTO.setTotalAlbuns(0L);
    }

    // ================= CREATE ===================

    @Test
    void deveCriarArtistaComSucesso() {
        when(artistaMapper.toEntity(requestDTO))
                .thenReturn(artista);

        when(artistaRepository.save(artista))
                .thenReturn(artista);

        when(artistaMapper.toResponse(artista))
                .thenReturn(responseDTO);

        ArtistaResponseDTO result = artistaService.criar(requestDTO);

        assertNotNull(result);
        assertEquals("Artista Teste", result.getNome());
        verify(artistaRepository).save(artista);
    }

    // ================= LIST ===================

    @Test
    void deveListarArtistasSemFiltro() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").ascending());
        Page<Artista> page = new PageImpl<>(List.of(artista));

        when(artistaRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        when(artistaMapper.toResumo(artista))
                .thenReturn(resumoDTO);

        Page<ArtistaResumoDTO> result =
                artistaService.listar(null, 0, 10, "asc");

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deveListarArtistasComFiltroPorNome() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").ascending());
        Page<Artista> page = new PageImpl<>(List.of(artista));

        when(artistaRepository.findByNomeContainingIgnoreCase("Art",
                pageable))
                .thenReturn(page);

        when(artistaMapper.toResumo(artista))
                .thenReturn(resumoDTO);

        Page<ArtistaResumoDTO> result =
                artistaService.listar("Art", 0, 10, "asc");

        assertEquals(1, result.getTotalElements());
    }

    // ================= GET BY ID ===================

    @Test
    void deveBuscarArtistaPorId() {
        when(artistaRepository.findById(1L))
                .thenReturn(Optional.of(artista));

        when(artistaMapper.toResponse(artista))
                .thenReturn(responseDTO);

        ArtistaResponseDTO result =
                artistaService.buscarPorId(1L);

        assertEquals("Artista Teste", result.getNome());
    }

    @Test
    void deveFalharAoBuscarArtistaInexistente() {
        when(artistaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> artistaService.buscarPorId(1L));
    }

    // ================= UPDATE ===================

    @Test
    void deveAtualizarArtistaComSucesso() {
        when(artistaRepository.findById(1L))
                .thenReturn(Optional.of(artista));

        when(artistaRepository.save(artista))
                .thenReturn(artista);

        when(artistaMapper.toResponse(artista))
                .thenReturn(responseDTO);

        ArtistaResponseDTO result =
                artistaService.atualizar(1L, requestDTO);

        assertEquals("Artista Teste", result.getNome());
        verify(artistaRepository).save(artista);
    }

    // ================= DELETE ===================

    @Test
    void deveDeletarArtista() {
        artistaService.deletar(1L);
        verify(artistaRepository).deleteById(1L);
    }
}
