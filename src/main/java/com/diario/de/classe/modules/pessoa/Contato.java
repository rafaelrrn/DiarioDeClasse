package com.diario.de.classe.modules.pessoa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/** Representa um meio de contato (telefone, e-mail, etc.) sem vínculo direto com uma Pessoa. */
@Entity
@Table(name = "contato")
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contato")
    private Long idContato;

    @Column(name = "tipo_contato")
    private String tipoContato;

    @Column(name = "contato")
    private String contato;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
