package br.gov.mt.seplag.artists_api.domain.repository;

import br.gov.mt.seplag.artists_api.api.dto.ArtistaResumoDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Artista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    @Query("""
        SELECT new br.gov.mt.seplag.artists_api.api.dto.ArtistaResumoDTO(
            a.id,
            a.nome,
            COUNT(al.id)
        )
        FROM Artista a
        LEFT JOIN a.albuns al
        WHERE (:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
        GROUP BY a.id, a.nome
    """)
    Page<ArtistaResumoDTO> listarComResumo(
            @Param("nome") String nome,
            Pageable pageable
    );
}
