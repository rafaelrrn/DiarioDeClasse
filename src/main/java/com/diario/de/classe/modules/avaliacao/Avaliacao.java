package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.calendario.CalendarioEscolar;
import com.diario.de.classe.modules.cronograma.PeriodoLetivo;
import com.diario.de.classe.modules.turma.Disciplina;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "avaliacao")
@Data
@EqualsAndHashCode(callSuper = true)
public class Avaliacao extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    private Long idAvaliacao;

    @ManyToOne
    @JoinColumn(name = "id_disciplina", nullable = false, referencedColumnName = "id_disciplina",
            foreignKey = @ForeignKey(name = "fk_id_disciplina"))
    private Disciplina disciplina;

    @ManyToOne
    @JoinColumn(name = "id_calendario_escolar", referencedColumnName = "id_calendario_escolar",
            foreignKey = @ForeignKey(name = "fk_id_calendario_escolar"))
    private CalendarioEscolar calendarioEscolar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo_letivo")
    private PeriodoLetivo periodoLetivo;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo = "PROVA";

    @Column(name = "materia")
    private String materia;

    @Column(name = "dia")
    private LocalDate dia;

    /**
     * Peso da avaliação para cálculo de média ponderada.
     * Exemplo: prova = 7, trabalho = 3 (soma = 10 → média em escala 0-10).
     * Quando nulo, assume peso 1 (todas as avaliações têm o mesmo valor).
     */
    @Column(name = "peso")
    private Integer peso;
}
