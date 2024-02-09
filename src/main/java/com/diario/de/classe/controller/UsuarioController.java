package com.diario.de.classe.controller;

import com.diario.de.classe.service.UsuarioService;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/usuario")
public class UsuarioController extends DefaultController {
    final private static Logger LOG = LogManager.getLogger(UsuarioController.class);

    @Autowired
    UsuarioService usuarioService;

    @ApiOperation(value = "Buscar todos usuários.", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosUsuarios() {
        return usuarioService.buscarTodosUsuarios();
    }
}