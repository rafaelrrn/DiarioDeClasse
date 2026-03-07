package com.diario.de.classe.modules.instituicao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/** Representa uma instituição de ensino (escola, colégio, etc.). */
@Entity
@Table(name = "instituicao_ensino")
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class InstituicaoEnsino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instituicao_ensino")
    private Long idInstituicaoEnsino;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "codigo_estadual")
    private String codigoEstadual;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
