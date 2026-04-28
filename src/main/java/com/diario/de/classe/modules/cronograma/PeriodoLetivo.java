package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "periodo_letivo")
@Data
@ToString(exclude = "cronogramaAnual")
@EqualsAndHashCode(callSuper = true, exclude = "cronogramaAnual")
public class PeriodoLetivo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_periodo_letivo")
    private Long idPeriodoLetivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cronograma_anual", nullable = false)
    private CronogramaAnual cronogramaAnual;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;
}
