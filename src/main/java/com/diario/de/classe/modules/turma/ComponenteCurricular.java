package com.diario.de.classe.modules.turma;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "componente_curricular")
@Data
@EqualsAndHashCode(callSuper = true)
public class ComponenteCurricular extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_componente_curricular")
    private Long idComponenteCurricular;

    @Column(name = "nome")
    private String nome;

    @Column(name = "obs")
    private String obs;
}
