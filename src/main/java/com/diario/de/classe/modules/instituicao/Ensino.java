package com.diario.de.classe.modules.instituicao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/** Nível de ensino (ex: Ensino Fundamental, Ensino Médio). */
@Entity
@Table(name = "ensino")
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Ensino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ensino")
    private Long idEnsino;

    @Column(name = "nome")
    private String nome;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
