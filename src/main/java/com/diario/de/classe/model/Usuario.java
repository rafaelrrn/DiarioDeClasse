package com.diario.de.classe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "usuario")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nome_completo")
    private String nomeCompleto;

    @Column(name="cpf", unique = true)
    private String cpf;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="senha")
    private String senha;

    public enum Perfil {
        admin, professor, aluno
    }

    @Column(name="perfil", columnDefinition = "ENUM('admin', 'professor', 'aluno')")
    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    @Column(name="data_expiracao_ultimo_token")
    private Date dataExpiracaoUltimoToken;

    @Column(name="data_ultimo_login")
    private Date dataUltimoLogin;

    @Column(name="quantidade_erros_de_senha")
    private Integer quantidadeErrosDeSenha=0;

    @Column(name="ativo")
    private String ativo;

    @Column(name="verificado")
    private String verificado;

    @Column(name = "created_at" ,nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
