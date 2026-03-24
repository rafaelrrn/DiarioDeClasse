package com.diario.de.classe.shared.exception;

import com.diario.de.classe.shared.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Centralizador de tratamento de exceções da API.
 *
 * Intercepta exceções lançadas em qualquer Controller e retorna respostas
 * padronizadas com ApiResponse, evitando que stacktraces vazem para o cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata recursos não encontrados — HTTP 404.
     * Lançada quando um ID não existe no banco.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Trata violações de regra de negócio — HTTP 422.
     * Exemplos: aluno já matriculado, frequência duplicada.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Trata falhas de validação de DTOs anotados com @Valid — HTTP 400.
     * Concatena todas as mensagens de validação em uma única string.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message));
    }

    /**
     * Fallback para exceções não tratadas — HTTP 500.
     * Não expõe detalhes técnicos ao cliente; registrar o erro no log é responsabilidade
     * do RequestLoggingInterceptor ou de um sistema de observabilidade.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ex.getMessage()));
    }
}
