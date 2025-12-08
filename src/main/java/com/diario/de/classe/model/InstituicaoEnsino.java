package com.diario.de.classe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Date;


//Anotação do springboot para informar que é classe é uma entidade/tabela
@Entity

//Informando ao springboot qual o nome da tabela no banco
@Table(name = "instituicao_ensino")

//Anotação do springboot/lombok para realizar os getters e setters
@Data

//----
@ToString

@EntityListeners(AuditingEntityListener.class)

public class InstituicaoEnsino {

    //Anotação do springboot que informa que o campo abaixo é a chave primária
    @Id

    //Anotação do springboot que determina a forma/estratégia do auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    //Anotação utilizada para informar o nome das colunas da tabela
    @Column(name="id_instituicao_ensino")
    //Apelido da coluna do banco para o java
    private Long idInstituicaoEnsino;

    @Column(name="descricao")
    private String descricao;

    @Column(name="codigo_estadual")
    private String codigoEstadual;

    @Column(name = "created_at" ,nullable = false, updatable = false)
    //Padronização para o tipo de data da coluna acima
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

}
