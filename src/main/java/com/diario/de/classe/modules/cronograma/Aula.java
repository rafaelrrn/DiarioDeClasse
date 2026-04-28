package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.frequencia.AlunoFrequencia;
import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.modules.turma.Classe;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aula")
@Data
@ToString(exclude = {"classe", "periodoLetivo", "registradoPor", "frequencias"})
@EqualsAndHashCode(callSuper = true, exclude = {"classe", "periodoLetivo", "registradoPor", "frequencias"})
public class Aula extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula")
    private Long idAula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classe", nullable = false)
    private Classe classe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo_letivo", nullable = false)
    private PeriodoLetivo periodoLetivo;

    @Column(name = "data_aula", nullable = false)
    private LocalDate dataAula;

    @Column(name = "numero_aula", nullable = false)
    private Integer numeroAula;

    @Column(name = "conteudo_ministrado", length = 500)
    private String conteudoMinistrado;

    @Column(name = "chamada_encerrada", nullable = false)
    private Boolean chamadaEncerrada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por", nullable = false)
    private Pessoa registradoPor;

    @OneToMany(mappedBy = "aula", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AlunoFrequencia> frequencias = new ArrayList<>();
}
