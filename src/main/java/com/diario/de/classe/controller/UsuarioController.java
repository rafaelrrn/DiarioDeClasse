package com.diario.de.classe.controller;

import com.diario.de.classe.facade.UsuarioFacade;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/usuario")

public class UsuarioController extends DefaultController {
    final private static Logger LOG = LogManager.getLogger(UsuarioController.class);

    @Autowired
    UsuarioFacade usuarioFacade;

    @ApiOperation(value = "Buscar todos usuários.", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosUsuarios()
    {
        return usuarioFacade.buscarTodosUsuarios();
    }
}