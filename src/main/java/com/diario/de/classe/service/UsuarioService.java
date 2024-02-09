package com.diario.de.classe.service;

import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    ResponseEntity<Object> buscarTodosUsuarios();
}
