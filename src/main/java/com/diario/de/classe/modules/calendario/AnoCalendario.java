package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ano_calendario")
@Data
@EqualsAndHashCode(callSuper = true)
public class AnoCalendario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ano_calendario")
    private Long idAnoCalendario;

    @Column(name = "ano")
    private String ano;
}
