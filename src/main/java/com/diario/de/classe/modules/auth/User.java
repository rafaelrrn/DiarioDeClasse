package com.diario.de.classe.modules.auth;

import com.diario.de.classe.shared.BaseEntity;
import com.diario.de.classe.shared.security.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Entidade que representa um usuário com acesso ao sistema.
 *
 * Separada de Pessoa para isolar a autenticação do domínio pedagógico.
 * O campo 'role' determina o nível de acesso conforme o enum Role.
 *
 * Soft delete: usuários desativados mantêm o histórico de ações no sistema
 * (herdado de BaseEntity).
 */
@Entity
@Table(name = "users")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

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
}
