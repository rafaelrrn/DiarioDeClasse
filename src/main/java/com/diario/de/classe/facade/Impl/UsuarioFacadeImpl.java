package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.facade.UsuarioFacade;
import com.diario.de.classe.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioFacadeImpl implements UsuarioFacade {

    @Autowired
    UsuarioService usuarioService;

    @Override
    public ResponseEntity<Object> buscarTodosUsuarios() {
        return  usuarioService.buscarTodosUsuarios();
    }
}
