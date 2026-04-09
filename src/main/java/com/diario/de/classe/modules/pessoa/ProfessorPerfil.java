package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Perfil específico de Professor — Extended Party Pattern.
 *
 * Contém dados exclusivos de quem é professor: registro no MEC,
 * formação acadêmica e data de admissão na instituição.
 *
 * Relação 1:1 com Pessoa. Nulo na Pessoa significa que ela não possui
 * perfil de professor ativo.
 */
@Entity
@Table(name = "professor_perfil")
@Data
@ToString(exclude = "pessoa")
@EqualsAndHashCode(callSuper = true, exclude = "pessoa")
public class ProfessorPerfil extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_professor_perfil")
    private Long idProfessorPerfil;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_professor_perfil_pessoa"))
    private Pessoa pessoa;

    @Column(name = "registro_mec", length = 30)
    private String registroMec;

    @Column(name = "formacao", length = 200)
    private String formacao;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;
}
