package br.gov.mt.seplag.artists_api.domain.repository;

import br.gov.mt.seplag.artists_api.domain.entity.Regional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionalRepository extends JpaRepository<Regional, Long> {

    Optional<Regional> findByIdAndAtivoTrue(Long id);

}
