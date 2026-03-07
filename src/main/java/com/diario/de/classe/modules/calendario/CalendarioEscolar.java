package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.modules.turma.Classe;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "calendario_escolar")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CalendarioEscolar {

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

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
