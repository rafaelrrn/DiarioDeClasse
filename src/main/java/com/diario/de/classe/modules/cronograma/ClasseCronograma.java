package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.turma.Classe;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "classe_cronograma")
@Data
@ToString(exclude = {"classe", "cronogramaAnual"})
@EqualsAndHashCode(callSuper = true, exclude = {"classe", "cronogramaAnual"})
public class ClasseCronograma extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_classe_cronograma")
    private Long idClasseCronograma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classe", nullable = false)
    private Classe classe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cronograma_anual", nullable = false)
    private CronogramaAnual cronogramaAnual;
}
