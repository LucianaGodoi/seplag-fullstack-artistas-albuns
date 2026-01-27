package br.gov.mt.seplag.artists_api.domain.service;

import br.gov.mt.seplag.artists_api.api.dto.AlbumNotificationDTO;
import br.gov.mt.seplag.artists_api.api.dto.AlbumRequestDTO;
import br.gov.mt.seplag.artists_api.api.dto.AlbumResponseDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Album;
import br.gov.mt.seplag.artists_api.domain.entity.Artista;
import br.gov.mt.seplag.artists_api.domain.repository.AlbumRepository;
import br.gov.mt.seplag.artists_api.domain.repository.ArtistaRepository;
import br.gov.mt.seplag.artists_api.mapper.AlbumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final AlbumMapper albumMapper;
    private final NotificationService notificationService;

    public AlbumResponseDTO criar(AlbumRequestDTO dto) {

        Artista artista = artistaRepository.findById(dto.getArtistaId())
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));

        Album album = albumMapper.toEntity(dto);
        album.setArtista(artista);

        Album salvo = albumRepository.save(album);

        notificationService.notificarNovoAlbum(
                new AlbumNotificationDTO(
                        salvo.getId(),
                        salvo.getNome(),
                        artista.getNome(),
                        LocalDateTime.now()
                )
        );
        return albumMapper.toResponse(salvo);
    }

    public Page<AlbumResponseDTO> listar(Long artistaId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());

        if (artistaId != null) {
            return albumRepository.findByArtistaId(artistaId, pageable)
                    .map(albumMapper::toResponse);
        }

        return albumRepository.findAll(pageable)
                .map(albumMapper::toResponse);
    }

    public AlbumResponseDTO buscarPorId(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));
        return albumMapper.toResponse(album);
    }

    public AlbumResponseDTO atualizar(Long id, AlbumRequestDTO dto) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

        albumMapper.updateEntity(dto, album);
        return albumMapper.toResponse(albumRepository.save(album));
    }

    public void deletar(Long id) {
        albumRepository.deleteById(id);
    }
}
