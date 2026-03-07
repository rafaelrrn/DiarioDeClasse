package com.diario.de.classe.modules.pessoa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/** Categoriza o tipo de uma Pessoa: ALUNO, PROFESSOR, RESPONSAVEL, etc. */
@Entity
@Table(name = "tipo_pessoa")
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class TipoPessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_pessoa")
    private Long idTipoPessoa;

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
