package com.diario.de.classe.modules.auth.dto;

import com.diario.de.classe.shared.security.Role;

/**
 * Resposta do endpoint GET /auth/me com os dados básicos do usuário autenticado.
 * Não expõe a senha nem dados sensíveis.
 */
public record UserMeResponse(
        Long idUser,
        String email,
        String nome,
        Role role
) {}
