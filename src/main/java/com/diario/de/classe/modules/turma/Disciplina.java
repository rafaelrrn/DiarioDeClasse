package com.diario.de.classe.modules.turma;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "disciplina")
@Data
@EqualsAndHashCode(callSuper = true)
public class Disciplina extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disciplina")
    private Long idDisciplina;

    @Column(name = "nome")
    private String nome;
}
