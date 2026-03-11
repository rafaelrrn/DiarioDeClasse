package com.diario.de.classe.shared.dto;

/**
 * Wrapper padrão para todas as respostas da API.
 *
 * Garante um formato JSON consistente para o frontend, independente do endpoint:
 *
 * Sucesso:
 * {
 *   "success": true,
 *   "data": { ... },
 *   "message": null,
 *   "error": null
 * }
 *
 * Erro:
 * {
 *   "success": false,
 *   "data": null,
 *   "message": null,
 *   "error": "Mensagem de erro"
 * }
 *
 * Uso no Controller:
 *   return ResponseEntity.ok(ApiResponse.ok(service.listar()));
 *   return ResponseEntity.status(CREATED).body(ApiResponse.ok(dto, "Criado com sucesso"));
 *   return ResponseEntity.ok(ApiResponse.error("Recurso não encontrado"));
 */
public record ApiResponse<T>(
        boolean success,
        T data,
        String message,
        Object error
) {

    /** Resposta de sucesso com dados, sem mensagem. */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    /** Resposta de sucesso com dados e mensagem descritiva (ex: "Cadastrado com sucesso"). */
    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    /** Resposta de erro com mensagem (data fica null). */
    public static ApiResponse<?> error(String errorMessage) {
        return new ApiResponse<>(false, null, null, errorMessage);
    }
}
