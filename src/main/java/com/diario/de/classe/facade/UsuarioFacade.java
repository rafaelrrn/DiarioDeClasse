package com.diario.de.classe.facade;

import org.springframework.http.ResponseEntity;

public interface UsuarioFacade {
    ResponseEntity<Object> buscarTodosUsuarios();
}
