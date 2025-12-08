package com.diario.de.classe.model;

import com.diario.de.classe.security.Role;
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
@Table(name = "users")

//Anotação do springboot/lombok para realizar os getters e setters
@Data

//----
@ToString

@EntityListeners(AuditingEntityListener.class)
public class User {

    //Anotação do springboot que informa que o campo abaixo é a chave primária
    @Id

    //Anotação do springboot que determina a forma/estratégia do auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //Anotação utilizada para informar o nome das colunas da tabela
    @Column(name="id_users")
    //Apelido da coluna do banco para o java
    private Long idUsers;

    @Column(name="nome")
    private String nome;

    @Column(name="email")
    private String email;

    @Column(name="senha")
    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

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
