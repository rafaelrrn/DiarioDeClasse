package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Representa um curso oferecido pela instituição.
 * Combina Ensino + Grau + Serie para identificar o curso completo
 * (ex: Ensino Fundamental + 1º Grau + 5ª Série).
 */
@Entity
@Table(name = "curso")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Curso extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Long idCurso;

    @ManyToOne
    @JoinColumn(name = "id_ensino", nullable = false, referencedColumnName = "id_ensino",
            foreignKey = @ForeignKey(name = "fk_id_ensino"))
    private Ensino ensino;

    @ManyToOne
    @JoinColumn(name = "id_grau", nullable = false, referencedColumnName = "id_grau",
            foreignKey = @ForeignKey(name = "fk_id_grau"))
    private Grau grau;

    @ManyToOne
    @JoinColumn(name = "id_serie", nullable = false, referencedColumnName = "id_serie",
            foreignKey = @ForeignKey(name = "fk_id_serie"))
    private Serie serie;
}
