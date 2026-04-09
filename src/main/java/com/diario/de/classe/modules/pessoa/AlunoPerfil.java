package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Perfil específico de Aluno — Extended Party Pattern.
 *
 * Contém dados exclusivos de quem é aluno: matrícula, data de matrícula
 * e informações sobre necessidades educacionais especiais (NEE).
 *
 * Relação 1:1 com Pessoa. Nulo na Pessoa significa que ela não possui
 * perfil de aluno ativo.
 */
@Entity
@Table(name = "aluno_perfil")
@Data
@ToString(exclude = "pessoa")
@EqualsAndHashCode(callSuper = true, exclude = "pessoa")
public class AlunoPerfil extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno_perfil")
    private Long idAlunoPerfil;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_aluno_perfil_pessoa"))
    private Pessoa pessoa;

    @Column(name = "matricula", nullable = false, unique = true, length = 30)
    private String matricula;

    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula;

    @Column(name = "necessidade_especial", nullable = false)
    private Boolean necessidadeEspecial = false;

    @Column(name = "descricao_nee", columnDefinition = "TEXT")
    private String descricaoNee;
}
