package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Usuario;
import com.diario.de.classe.repository.jpa.UsuarioRepositoryJpa;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;

    @Autowired
    UsuarioRepositoryJpa usuarioRepositoryJpa;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;

    @Override
    public ResponseEntity<Object> buscarTodosUsuarios() {
        List<Usuario> allUsers = usuarioRepositoryJpa.findAll();

        if (allUsers.isEmpty()) return responseDiarioDeClasse.error(NOT_FOUND_MSG, HttpStatus.NOT_FOUND);
        return responseDiarioDeClasse.success(allUsers, HttpStatus.OK);
    }
}
