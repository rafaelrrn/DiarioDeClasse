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
@Table(name = "classe")

//Anotação do springboot/lombok para realizar os getters e setters
@Data

//----
@ToString

@EntityListeners(AuditingEntityListener.class)
public class Classe {

    //Anotação do springboot que informa que o campo abaixo é a chave primária
    @Id

    //Anotação do springboot que determina a forma/estratégia do auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //Anotação utilizada para informar o nome das colunas da tabela
    @Column(name="id_classe")

    //Apelido da coluna do banco para o java
    private Long idClasse;


    @ManyToOne
    @JoinColumn(name = "id_instituicao_ensino", nullable = false, referencedColumnName = "id_instituicao_ensino", foreignKey = @ForeignKey(name = "fk_id_instituicao_ensino"))
    private InstituicaoEnsino instituicaoEnsino;

    @ManyToOne
    @JoinColumn(name = "id_componente_curricular", referencedColumnName = "id_componente_curricular", foreignKey = @ForeignKey(name = "fk_id_componente_curricular"))
    private ComponenteCurricular componenteCurricular;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false, referencedColumnName = "id_curso", foreignKey = @ForeignKey(name = "fk_id_curso"))
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "id_turno", nullable = false, referencedColumnName = "id_turno", foreignKey = @ForeignKey(name = "fk_id_turno"))
    private Turno turno;

    @ManyToOne
    @JoinColumn(name = "id_turma", nullable = false, referencedColumnName = "id_turma", foreignKey = @ForeignKey(name = "fk_id_turma"))
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "id_professor", nullable = false, referencedColumnName = "id_pessoa", foreignKey = @ForeignKey(name = "fk_id_professor"))
    private Pessoa pessoa;

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
