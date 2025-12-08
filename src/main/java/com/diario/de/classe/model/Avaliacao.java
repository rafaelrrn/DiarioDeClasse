package com.diario.de.classe.model;

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
@Table(name = "avaliacao")

//Anotação do springboot/lombok para realizar os getters e setters
@Data

//----
@ToString

@EntityListeners(AuditingEntityListener.class)
public class Avaliacao {

    //Anotação do springboot que informa que o campo abaixo é a chave primária
    @Id

    //Anotação do springboot que determina a forma/estratégia do auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //Anotação utilizada para informar o nome das colunas da tabela
    @Column(name="id_avaliacao")

    //Apelido da coluna do banco para o java
    private Long idAvaliacao;


    @ManyToOne
    @JoinColumn(name = "id_disciplina", nullable = false, referencedColumnName = "id_disciplina", foreignKey = @ForeignKey(name = "fk_id_disciplina"))
    private Disciplina disciplina;

    @ManyToOne
    @JoinColumn(name = "id_calendario_escolar", referencedColumnName = "id_calendario_escolar", foreignKey = @ForeignKey(name = "fk_id_calendario_escolar"))
    private CalendarioEscolar calendarioEscolar;

    @Column(name="materia")
    private String materia;

    @Column(name="dia")
    private String dia;

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
