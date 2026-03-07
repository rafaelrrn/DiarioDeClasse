package com.diario.de.classe.modules.pessoa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/** Tabela de associação entre Pessoa e Endereco (N:N com atributo 'nome'). */
@Entity
@Table(name = "endereco_pessoa")
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class EnderecoPessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco_pessoa")
    private Long idEnderecoPessoa;

    @ManyToOne
    @JoinColumn(name = "id_pessoa", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_pessoa"))
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "id_endereco", nullable = false, referencedColumnName = "id_endereco",
            foreignKey = @ForeignKey(name = "fk_id_endereco"))
    private Endereco endereco;

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
