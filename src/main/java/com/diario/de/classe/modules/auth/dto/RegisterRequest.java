package com.diario.de.classe.modules.auth.dto;

import com.diario.de.classe.shared.security.Role;

/**
 * Dados necessários para registrar um novo usuário.
 *
 * idPessoa é opcional: deve ser informado para professores, alunos e responsáveis
 * que já possuem registro no domínio pedagógico. Pode ser nulo para administradores técnicos.
 */
public record RegisterRequest(
        String nome,
        String email,
        String senha,
        Role role,
        Long idPessoa
) {}
