package br.gov.mt.seplag.artists_api.domain.service;

import br.gov.mt.seplag.artists_api.api.dto.AlbumImageResponseDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Album;
import br.gov.mt.seplag.artists_api.domain.entity.ImagemAlbum;
import br.gov.mt.seplag.artists_api.domain.repository.AlbumRepository;
import br.gov.mt.seplag.artists_api.domain.repository.ImagemAlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagemAlbumService {

    private final AlbumRepository albumRepository;
    private final ImagemAlbumRepository imagemAlbumRepository;
    private final StorageService storageService;

    public List<AlbumImageResponseDTO> upload(Long albumId, List<MultipartFile> files) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

        for (MultipartFile file : files) {
            try {
                String objectKey = storageService.uploadAlbumCover(
                        albumId,
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getInputStream(),
                        file.getSize()
                );

                ImagemAlbum imagem = ImagemAlbum.builder()
                        .album(album)
                        .objectKey(objectKey)
                        .originalFilename(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .build();

                imagemAlbumRepository.save(imagem);

            } catch (Exception e) {
                throw new RuntimeException("Erro ao enviar imagem", e);
            }
        }

        return listar(albumId);
    }

    public List<AlbumImageResponseDTO> listar(Long albumId) {
        return imagemAlbumRepository.findAll().stream()
                .filter(img -> img.getAlbum().getId().equals(albumId))
                .map(img -> new AlbumImageResponseDTO(
                        img.getId(),
                        img.getOriginalFilename(),
                        img.getContentType(),
                        storageService.presignedGetUrl(
                                img.getObjectKey(),
                                Duration.ofMinutes(30)
                        )
                ))
                .toList();
    }
}
