package br.gov.mt.seplag.artists_api.domain.service;

import br.gov.mt.seplag.artists_api.api.dto.AlbumRequestDTO;
import br.gov.mt.seplag.artists_api.api.dto.AlbumResponseDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Album;
import br.gov.mt.seplag.artists_api.domain.entity.Artista;
import br.gov.mt.seplag.artists_api.domain.repository.AlbumRepository;
import br.gov.mt.seplag.artists_api.domain.repository.ArtistaRepository;
import br.gov.mt.seplag.artists_api.mapper.AlbumMapper;
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
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistaRepository artistaRepository;

    @Mock
    private AlbumMapper albumMapper;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AlbumService albumService;

    private Artista artista;
    private Album album;
    private AlbumRequestDTO requestDTO;
    private AlbumResponseDTO responseDTO;

    @BeforeEach
    void setup() {

        artista = Artista.builder()
                .id(1L)
                .nome("Artista Teste")
                .build();

        album = Album.builder()
                .id(10L)
                .nome("Album Teste")
                .artista(artista)
                .build();

        requestDTO = new AlbumRequestDTO();
        requestDTO.setNome("Album Teste");
        requestDTO.setArtistaId(1L);

        responseDTO = new AlbumResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setNome("Album Teste");
        responseDTO.setAnoLancamento(2024);
    }



    // ================= CRIAR =================

    @Test
    void deveCriarAlbumComSucesso() {

        when(artistaRepository.findById(1L))
                .thenReturn(Optional.of(artista));

        when(albumMapper.toEntity(any()))
                .thenReturn(album);

        when(albumRepository.save(any()))
                .thenReturn(album);

        when(albumMapper.toResponse(any()))
                .thenReturn(responseDTO);

        doNothing()
                .when(notificationService)
                .notificarNovoAlbum(any());

        AlbumResponseDTO result = albumService.criar(requestDTO);

        assertNotNull(result);
        assertEquals("Album Teste", result.getNome());

        verify(albumRepository).save(any());
    }

    @Test
    void deveFalharAoCriarQuandoArtistaNaoExiste() {
        when(artistaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> albumService.criar(requestDTO));
    }

    // ================= LISTAR =================

    @Test
    void deveListarAlbunsSemFiltro() {
        Page<Album> page = new PageImpl<>(List.of(album));

        when(albumRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        when(albumMapper.toResponse(album))
                .thenReturn(responseDTO);

        Page<AlbumResponseDTO> result =
                albumService.listar(null, 0, 10);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deveListarAlbunsPorArtista() {
        Page<Album> page = new PageImpl<>(List.of(album));

        when(albumRepository.findByArtistaId(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        when(albumMapper.toResponse(album))
                .thenReturn(responseDTO);

        Page<AlbumResponseDTO> result =
                albumService.listar(1L, 0, 10);

        assertEquals(1, result.getTotalElements());
    }

    // ================= BUSCAR =================

    @Test
    void deveBuscarAlbumPorId() {
        when(albumRepository.findById(10L))
                .thenReturn(Optional.of(album));

        when(albumMapper.toResponse(album))
                .thenReturn(responseDTO);

        AlbumResponseDTO result = albumService.buscarPorId(10L);

        assertEquals("Album Teste", result.getNome());
    }

    @Test
    void deveFalharAoBuscarAlbumInexistente() {
        when(albumRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> albumService.buscarPorId(10L));
    }

    // ================= ATUALIZAR =================

    @Test
    void deveAtualizarAlbumComSucesso() {
        when(albumRepository.findById(10L))
                .thenReturn(Optional.of(album));

        when(albumRepository.save(album))
                .thenReturn(album);

        when(albumMapper.toResponse(album))
                .thenReturn(responseDTO);

        AlbumResponseDTO result =
                albumService.atualizar(10L, requestDTO);

        assertEquals("Album Teste", result.getNome());
        verify(albumMapper).updateEntity(requestDTO, album);
    }

    // ================= DELETAR =================

    @Test
    void deveDeletarAlbum() {
        doNothing().when(albumRepository).deleteById(10L);

        albumService.deletar(10L);

        verify(albumRepository).deleteById(10L);
    }
}
