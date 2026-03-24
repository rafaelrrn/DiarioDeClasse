package com.diario.de.classe.modules.auth.dto;

import com.diario.de.classe.shared.security.Role;

/**
 * Dados atualizáveis de um usuário existente.
 *
 * idPessoa aceita null para desvincular a Pessoa do usuário.
 * senha é opcional: se nula ou vazia, a senha atual é mantida.
 */
public record UserUpdateRequest(
        String nome,
        String email,
        String senha,
        Role role,
        Long idPessoa
) {}
