package com.diario.de.classe.model;

import com.diario.de.classe.dto.PessoaDTO;
import com.diario.de.classe.service.PessoaService;
import com.diario.de.classe.service.TipoPessoaService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


//Anotação do springboot para informar que é classe é uma entidade/tabela
@Entity

//Informando ao springboot qual o nome da tabela no banco
@Table(name = "pessoa")

//Anotação do springboot/lombok para realizar os getters e setters
@Data

//----
@ToString

@EntityListeners(AuditingEntityListener.class)

public class Pessoa {

    //Anotação do springboot que informa que o campo abaixo é a chave primária
    @Id

    //Anotação do springboot que determina a forma/estratégia do auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //Anotação utilizada para informar o nome das colunas da tabela
    @Column(name="id_pessoa")
    //Apelido da coluna do banco para o java
    private Long idPessoa;

    @ManyToOne
    @JoinColumn(name = "id_tipo_pessoa", nullable = false, referencedColumnName = "id_tipo_pessoa", foreignKey = @ForeignKey(name = "fk_id_tipo_pessoa"))
    private TipoPessoa tipoPessoa;

    @Column(name="nome")
    private String nome;

    @Column(name="sexo")
    private String sexo;

    @Column(name="data_nascimento")
    private String dataNascimento;

    @Column(name="situacao")
    private String situacao;

    @Column(name="obs")
    private String obs;

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
