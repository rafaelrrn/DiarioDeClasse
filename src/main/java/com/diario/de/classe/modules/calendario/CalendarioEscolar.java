package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.modules.turma.Classe;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "calendario_escolar")
@Data
@EqualsAndHashCode(callSuper = true)
public class CalendarioEscolar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calendario_escolar")
    private Long idCalendarioEscolar;

    @ManyToOne
    @JoinColumn(name = "id_mes", nullable = false, referencedColumnName = "id_mes",
            foreignKey = @ForeignKey(name = "fk_id_mes"))
    private Mes mes;

    @ManyToOne
    @JoinColumn(name = "id_ano_calendario", referencedColumnName = "id_ano_calendario",
            foreignKey = @ForeignKey(name = "fk_id_ano_calendario"))
    private AnoCalendario anoCalendario;

    @ManyToOne
    @JoinColumn(name = "id_periodo", nullable = false, referencedColumnName = "id_periodo",
            foreignKey = @ForeignKey(name = "fk_id_periodo"))
    private Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "id_classe", nullable = false, referencedColumnName = "id_classe",
            foreignKey = @ForeignKey(name = "fk_id_classe"))
    private Classe classe;

    @Column(name = "dias_letivos")
    private String diasLetivos;

    @Column(name = "dias_avaliacoes")
    private String diasAvaliacoes;
}
