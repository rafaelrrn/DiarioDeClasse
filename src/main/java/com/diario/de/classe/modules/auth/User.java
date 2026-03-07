package com.diario.de.classe.modules.auth;

import com.diario.de.classe.shared.security.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Entidade que representa um usuário com acesso ao sistema.
 *
 * Separada de Pessoa para isolar a autenticação do domínio pedagógico.
 * O campo 'role' determina o nível de acesso conforme o enum Role.
 *
 * TODO (Etapa 2): Migrar para estender BaseEntity e adicionar soft delete.
 */
@Entity
@Table(name = "users")
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users")
    private Long idUsers;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    /** Perfil de acesso — define as permissões via @PreAuthorize */
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
