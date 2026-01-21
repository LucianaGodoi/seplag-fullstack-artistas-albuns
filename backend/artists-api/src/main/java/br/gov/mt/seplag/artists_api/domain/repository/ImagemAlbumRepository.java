package br.gov.mt.seplag.artists_api.domain.repository;

import br.gov.mt.seplag.artists_api.domain.entity.ImagemAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemAlbumRepository extends JpaRepository<ImagemAlbum, Long> {
}
