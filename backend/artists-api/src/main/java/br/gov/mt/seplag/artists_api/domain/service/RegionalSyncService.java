package br.gov.mt.seplag.artists_api.domain.service;

import br.gov.mt.seplag.artists_api.api.dto.RegionalApiDTO;
import br.gov.mt.seplag.artists_api.domain.entity.Regional;
import br.gov.mt.seplag.artists_api.domain.repository.RegionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionalSyncService {

    private final RegionalRepository repository;

    private static final String URL =
            "https://integrador-argus-api.geia.vip/v1/regionais";

    public void sincronizar() {

        RestTemplate restTemplate = new RestTemplate();

        RegionalApiDTO[] response =
                restTemplate.getForObject(URL, RegionalApiDTO[].class);

        List<RegionalApiDTO> externas =
                response == null
                        ? Collections.emptyList()
                        : Arrays.asList(response);

        Map<Long, RegionalApiDTO> externasMap =
                externas.stream()
                        .collect(Collectors.toMap(
                                RegionalApiDTO::getId,
                                r -> r
                        ));

        List<Regional> locais = repository.findAll();

        // 1) Inativar quem não existe mais
        for (Regional local : locais) {
            if (!externasMap.containsKey(local.getId()) && local.getAtivo()) {
                local.setAtivo(false);
                repository.save(local);
            }
        }

        // 2) Inserir novos ou tratar alteração
        for (RegionalApiDTO dto : externas) {

            Optional<Regional> existenteOpt =
                    repository.findByIdAndAtivoTrue(dto.getId());

            if (existenteOpt.isEmpty()) {
                // novo
                repository.save(
                        Regional.builder()
                                .id(dto.getId())
                                .nome(dto.getNome())
                                .ativo(true)
                                .build()
                );
                continue;
            }

            Regional existente = existenteOpt.get();

            if (!existente.getNome().equals(dto.getNome())) {
                // nome mudou -  inativa antigo
                existente.setAtivo(false);
                repository.save(existente);

                // cria novo
                repository.save(
                        Regional.builder()
                                .id(dto.getId())
                                .nome(dto.getNome())
                                .ativo(true)
                                .build()
                );
            }
        }
    }
}
