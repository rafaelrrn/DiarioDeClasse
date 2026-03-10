package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.calendario.CalendarioEscolar;
import com.diario.de.classe.modules.turma.Disciplina;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @Column(name = "materia")
    private String materia;

    @Column(name = "dia")
    private String dia;
}
