package com.diario.de.classe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "CLS002")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString
public class ClasseEscolar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CodCls")
    private Long codCls;

    @Column(name="AnoCls")
    private Integer anoCls;

    public enum TrnCls {
        manha, tarde, noite
    }

    @Column(name="TrnCls", columnDefinition = "ENUM('manha', 'tarde', 'noite')")
    @Enumerated(EnumType.STRING)
    private TrnCls trnCls;

    public enum EsnCls {
        ensino_fundamental, ensino_medio, tecnico_em_contabilidade
    }

    @Column(name="EsnCls", columnDefinition = "ENUM('ensino_fundamental', 'ensino_medio', 'tecnico_em_contabilidade')")
    @Enumerated(EnumType.STRING)
    private EsnCls esnCls;

    public enum SreCls {
        fun_1_ano, fun_2_ano, fun_3_ano, fun_4_ano, fun_5_ano, fun_6_ano, fun_7_ano, fun_8_ano, fun_9_ano,
        med_1_ano, med_2_ano, med_3_ano,
        tec_1_sem, tec_2_sem, tec_3_sem
    }

    @Column(name="SreCls", columnDefinition = "ENUM('fun_1_ano', 'fun_2_ano', 'fun_3_ano', 'fun_4_ano', 'fun_5_ano', 'fun_6_ano', 'fun_7_ano', 'fun_8_ano', 'fun_9_ano', 'med_1_ano', 'med_2_ano', 'med_3_ano', 'tec_1_sem', 'tec_2_sem', 'tec_3_sem')")
    @Enumerated(EnumType.STRING)
    private SreCls sreCls;

    @Column(name="TrmCls")
    private String trmCls;

}
