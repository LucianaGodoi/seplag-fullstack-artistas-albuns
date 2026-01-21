package br.gov.mt.seplag.artists_api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "regional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regional extends BaseEntity {

    @Id
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;
}
