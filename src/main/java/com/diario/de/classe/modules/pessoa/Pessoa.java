package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Representa qualquer pessoa no sistema (aluno, professor, responsável, etc.).
 *
 * O tipo é determinado pelo relacionamento com TipoPessoa.
 * Alunos são Pessoas do tipo ALUNO; professores são Pessoas do tipo PROFESSOR, etc.
 *
 * Perfis específicos (Extended Party Pattern):
 * - AlunoPerfil: dados exclusivos de alunos (matrícula, NEE)
 * - ProfessorPerfil: dados exclusivos de professores (registro MEC, formação)
 * - ResponsavelPerfil: dados exclusivos de responsáveis (profissão, local de trabalho)
 * Cada perfil é opcional e carregado sob demanda (LAZY).
 *
 * Soft delete: ao invés de excluir, o campo 'ativo' é definido como false
 * e 'deletedAt' recebe a data da desativação (herdado de BaseEntity).
 */
@Entity
@Table(name = "pessoa")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Pessoa extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa")
    private Long idPessoa;

    @ManyToOne
    @JoinColumn(name = "id_tipo_pessoa", nullable = false, referencedColumnName = "id_tipo_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_tipo_pessoa"))
    private TipoPessoa tipoPessoa;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf", unique = true, length = 11)
    private String cpf;

    @Column(name = "sexo", length = 2)
    private String sexo;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "situacao", length = 30)
    private String situacao;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "obs")
    private String obs;

    // -------------------------------------------------------------------------
    // Perfis específicos — Extended Party Pattern
    // Carregados sob demanda (LAZY). Nulos para pessoas sem esse perfil.
    // -------------------------------------------------------------------------

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AlunoPerfil alunoPerfil;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProfessorPerfil professorPerfil;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ResponsavelPerfil responsavelPerfil;
}
