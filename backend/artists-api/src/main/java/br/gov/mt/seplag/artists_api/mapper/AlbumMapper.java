package br.gov.mt.seplag.artists_api.mapper;

import br.gov.mt.seplag.artists_api.api.dto.AlbumCoverResponseDTO;
import br.gov.mt.seplag.artists_api.api.dto.AlbumRequestDTO;
import br.gov.mt.seplag.artists_api.api.dto.AlbumResponseDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Album;
import br.gov.mt.seplag.artists_api.domain.service.MinioService;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

//    @Mapping(target = "artista", ignore = true)
//    Album toEntity(AlbumRequestDTO dto);
//
//    AlbumResponseDTO toResponse(Album entity);
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateEntity(AlbumRequestDTO dto, @MappingTarget Album entity);
@Mapping(target = "artista", ignore = true)
Album toEntity(AlbumRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(AlbumRequestDTO dto, @MappingTarget Album entity);

    default AlbumResponseDTO toResponse(
            Album album,
            @Context MinioService minioService
    ) {

        List<AlbumCoverResponseDTO> capas =
                album.getImagens() == null
                        ? List.of()
                        : album.getImagens()
                        .stream()
                        .map(img -> new AlbumCoverResponseDTO(
                                img.getId(),
                                minioService.gerarPresignedUrl(img.getObjectKey())
                        ))
                        .toList();

        AlbumResponseDTO dto = new AlbumResponseDTO();
        dto.setId(album.getId());
        dto.setNome(album.getNome());
        dto.setAnoLancamento(album.getAnoLancamento());
        dto.setCapas(capas);

        return dto;
    }
}
