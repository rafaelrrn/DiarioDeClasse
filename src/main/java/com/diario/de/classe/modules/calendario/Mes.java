package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "mes")
@Data
@EqualsAndHashCode(callSuper = true)
public class Mes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mes")
    private Long idMes;

    @Column(name = "nome")
    private String nome;
}
