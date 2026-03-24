package com.diario.de.classe.modules.auth;

import com.diario.de.classe.modules.pessoa.Pessoa;
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
 * O vínculo com Pessoa é opcional: administradores técnicos podem existir
 * sem uma Pessoa correspondente. Para professores, alunos e responsáveis,
 * o idPessoa permite que o sistema identifique o registro pedagógico do usuário.
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

    /**
     * Pessoa do domínio pedagógico vinculada a este usuário (opcional).
     * Nulo para administradores técnicos sem registro pedagógico.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", nullable = true,
            foreignKey = @ForeignKey(name = "fk_users_pessoa"))
    private Pessoa pessoa;
}
