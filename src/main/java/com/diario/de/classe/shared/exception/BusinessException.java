package com.diario.de.classe.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Lançada quando uma regra de negócio é violada.
 * Mapeada para HTTP 422 (Unprocessable Entity) pelo GlobalExceptionHandler.
 *
 * Exemplos de uso:
 * - Aluno já matriculado nesta turma
 * - Frequência já lançada para esta aula
 * - Média inválida para o período
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
