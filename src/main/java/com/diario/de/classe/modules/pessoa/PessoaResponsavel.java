package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Associação entre um aluno (Pessoa do tipo ALUNO) e seu responsável legal.
 *
 * Um aluno pode ter múltiplos responsáveis (pai, mãe, tutor), por isso usa
 * uma tabela separada com o campo 'parentesco' para identificar o vínculo.
 */
@Entity
@Table(name = "pessoa_responsavel")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class PessoaResponsavel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa_responsavel")
    private Long idPessoaResponsavel;

    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_aluno"))
    private Pessoa pessoaAluno;

    @ManyToOne
    @JoinColumn(name = "id_responsavel", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_responsavel"))
    private Pessoa pessoaResponsavel;

    @Column(name = "parentesco")
    private String parentesco;
}
