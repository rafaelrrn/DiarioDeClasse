package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Raiz do calendário institucional — representa o ano letivo completo.
 *
 * Contém os períodos letivos (bimestres, trimestres, semestres), os eventos
 * do calendário (feriados, recessos) e as classes vinculadas.
 *
 * Status possíveis: RASCUNHO → ATIVO → ENCERRADO
 */
@Entity
@Table(name = "cronograma_anual")
@Data
@ToString(exclude = {"periodosLetivos", "eventosCalendario", "classesCronograma"})
@EqualsAndHashCode(callSuper = true, exclude = {"periodosLetivos", "eventosCalendario", "classesCronograma"})
public class CronogramaAnual extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cronograma_anual")
    private Long idCronogramaAnual;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "dias_letivos_previstos")
    private Integer diasLetivosPrevistos;

    @Column(name = "carga_horaria_prevista")
    private Integer cargaHorariaPrevista;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "RASCUNHO";

    @OneToMany(mappedBy = "cronogramaAnual", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PeriodoLetivo> periodosLetivos = new ArrayList<>();

    @OneToMany(mappedBy = "cronogramaAnual", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EventoCalendario> eventosCalendario = new ArrayList<>();

    @OneToMany(mappedBy = "cronogramaAnual", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClasseCronograma> classesCronograma = new ArrayList<>();
}
