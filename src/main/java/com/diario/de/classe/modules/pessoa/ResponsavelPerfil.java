package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Perfil específico de Responsável — Extended Party Pattern.
 *
 * Contém dados exclusivos de quem é responsável legal por um aluno:
 * profissão e local de trabalho (úteis para contato em emergências).
 *
 * Relação 1:1 com Pessoa. Nulo na Pessoa significa que ela não possui
 * perfil de responsável ativo.
 */
@Entity
@Table(name = "responsavel_perfil")
@Data
@ToString(exclude = "pessoa")
@EqualsAndHashCode(callSuper = true, exclude = "pessoa")
public class ResponsavelPerfil extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_responsavel_perfil")
    private Long idResponsavelPerfil;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_responsavel_perfil_pessoa"))
    private Pessoa pessoa;

    @Column(name = "profissao", length = 100)
    private String profissao;

    @Column(name = "local_trabalho", length = 200)
    private String localTrabalho;
}
