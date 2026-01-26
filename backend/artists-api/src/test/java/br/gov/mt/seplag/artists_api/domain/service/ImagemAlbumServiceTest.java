package br.gov.mt.seplag.artists_api.domain.service;

import br.gov.mt.seplag.artists_api.api.dto.AlbumImageResponseDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Album;
import br.gov.mt.seplag.artists_api.domain.entity.ImagemAlbum;
import br.gov.mt.seplag.artists_api.domain.repository.AlbumRepository;
import br.gov.mt.seplag.artists_api.domain.repository.ImagemAlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImagemAlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ImagemAlbumRepository imagemAlbumRepository;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private ImagemAlbumService imagemAlbumService;

    private Album album;

    @BeforeEach
    void setup() {
        album = Album.builder()
                .id(1L)
                .nome("Album Teste")
                .build();
    }

    // ================= UPLOAD =====================

    @Test
    void deveRealizarUploadComSucesso() throws Exception {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "capa.jpg",
                        "image/jpeg",
                        "conteudo".getBytes()
                );

        when(albumRepository.findById(1L))
                .thenReturn(Optional.of(album));

        when(storageService.uploadAlbumCover(
                anyLong(),
                anyString(),
                anyString(),
                any(),
                anyLong()
        )).thenReturn("object-key");

        when(imagemAlbumRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(imagemAlbumRepository.findAll())
                .thenReturn(List.of(
                        ImagemAlbum.builder()
                                .id(10L)
                                .album(album)
                                .objectKey("object-key")
                                .originalFilename("capa.jpg")
                                .contentType("image/jpeg")
                                .build()
                ));

        when(storageService.presignedGetUrl(anyString(), any(Duration.class)))
                .thenReturn("http://url-assinada");

        List<AlbumImageResponseDTO> result =
                imagemAlbumService.upload(1L, List.of(file));

        assertEquals(1, result.size());
        assertEquals("capa.jpg", result.get(0).originalFilename());
        assertEquals("http://url-assinada", result.get(0).url());

        verify(imagemAlbumRepository).save(any());
    }

    @Test
    void deveFalharQuandoAlbumNaoExiste() {

        when(albumRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> imagemAlbumService.upload(1L, List.of()));

        assertEquals("Álbum não encontrado", ex.getMessage());
    }

    // ================= LISTAR =====================

    @Test
    void deveListarImagensDoAlbum() {

        when(imagemAlbumRepository.findAll())
                .thenReturn(List.of(
                        ImagemAlbum.builder()
                                .id(1L)
                                .album(album)
                                .objectKey("key1")
                                .originalFilename("img1.jpg")
                                .contentType("image/jpeg")
                                .build(),
                        ImagemAlbum.builder()
                                .id(2L)
                                .album(Album.builder().id(99L).build()) // outro álbum
                                .objectKey("key2")
                                .originalFilename("img2.jpg")
                                .contentType("image/jpeg")
                                .build()
                ));

        when(storageService.presignedGetUrl(anyString(), any(Duration.class)))
                .thenReturn("url");

        List<AlbumImageResponseDTO> result =
                imagemAlbumService.listar(1L);

        assertEquals(1, result.size());
        assertEquals("img1.jpg", result.get(0).originalFilename());
    }
}
