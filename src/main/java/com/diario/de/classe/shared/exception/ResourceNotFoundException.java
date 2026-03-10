package com.diario.de.classe.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Lançada quando um recurso solicitado não é encontrado no banco de dados.
 * Mapeada automaticamente para HTTP 404 pelo GlobalExceptionHandler.
 *
 * Uso: throw new ResourceNotFoundException("Aluno não encontrado com id: " + id);
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
