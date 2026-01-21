package br.gov.mt.seplag.artists_api.mapper;

import br.gov.mt.seplag.artists_api.api.dto.AlbumRequestDTO;
import br.gov.mt.seplag.artists_api.api.dto.AlbumResponseDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    @Mapping(target = "artista", ignore = true)
    Album toEntity(AlbumRequestDTO dto);

    AlbumResponseDTO toResponse(Album entity);
}
