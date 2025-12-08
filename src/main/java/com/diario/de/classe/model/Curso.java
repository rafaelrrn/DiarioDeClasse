package com.diario.de.classe.model;

import com.diario.de.classe.model.Grau;
import com.diario.de.classe.model.Serie;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


//Anotação do springboot para informar que é classe é uma entidade/tabela
@Entity

//Informando ao springboot qual o nome da tabela no banco
@Table(name = "curso")

//Anotação do springboot/lombok para realizar os getters e setters
@Data

//----
@ToString


@EntityListeners(AuditingEntityListener.class)

public class Curso {

    //Anotação do springboot que informa que o campo abaixo é a chave primária
    @Id

    //Anotação do springboot que determina a forma/estratégia do auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //Anotação utilizada para informar o nome das colunas da tabela
    @Column(name="id_curso")

    //Apelido da coluna do banco para o java
    private Long idCurso;


    @ManyToOne
    @JoinColumn(name = "id_ensino", nullable = false, referencedColumnName = "id_ensino", foreignKey = @ForeignKey(name = "fk_id_ensino"))
    private Ensino ensino;

    @ManyToOne
    @JoinColumn(name = "id_grau",nullable = false, referencedColumnName = "id_grau", foreignKey = @ForeignKey(name = "fk_id_grau"))
    private Grau grau;

    @ManyToOne
    @JoinColumn(name = "id_serie",nullable = false,  referencedColumnName = "id_serie", foreignKey = @ForeignKey(name = "fk_id_serie"))
    private Serie serie;

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
