package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.turma.Classe;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalTime;

@Entity
@Table(name = "grade_curricular")
@Data
@ToString(exclude = "classe")
@EqualsAndHashCode(callSuper = true, exclude = "classe")
public class GradeCurricular extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grade_curricular")
    private Long idGradeCurricular;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classe", nullable = false)
    private Classe classe;

    /** 1=Segunda, 2=Terça, 3=Quarta, 4=Quinta, 5=Sexta, 6=Sábado */
    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana;

    @Column(name = "numero_aula", nullable = false)
    private Integer numeroAula;

    @Column(name = "horario_inicio", nullable = false)
    private LocalTime horarioInicio;

    @Column(name = "horario_fim", nullable = false)
    private LocalTime horarioFim;
}
