package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "evento_calendario")
@Data
@ToString(exclude = "cronogramaAnual")
@EqualsAndHashCode(callSuper = true, exclude = "cronogramaAnual")
public class EventoCalendario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento_calendario")
    private Long idEventoCalendario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cronograma_anual", nullable = false)
    private CronogramaAnual cronogramaAnual;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "tipo_evento", nullable = false, length = 20)
    private String tipoEvento;

    @Column(name = "eh_letivo", nullable = false)
    private Boolean ehLetivo = false;

    @Column(name = "cor", length = 7)
    private String cor;
}
