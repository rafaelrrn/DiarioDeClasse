package com.diario.de.classe.controller;

import com.diario.de.classe.dto.EnsinoDTO;
import com.diario.de.classe.facade.EnsinoFacade;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@RestController eu estou definindo o tipo de api de arquitetura RestFul
@RestController

//Permite qualquer ip acessar o end-point
@CrossOrigin

//Endereço do url para o end-point
@RequestMapping("/tabela-de-ensino")

public class EnsinoController {

    @Autowired
    private EnsinoFacade ensinoFacade;

    @Operation(summary = "Buscar todos os Ensinos.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosEnsinos()
    {
       return ensinoFacade.buscarTodosEnsinos();
    }

    @Operation(summary = "Buscar Ensino por idEnsino.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idEnsino")
    public ResponseEntity<Object> buscarEnsinoPoridEnsino(@RequestParam(value = "idEnsino") Long idEnsino)
    {
        return ensinoFacade.buscarEnsinoPoridEnsino(idEnsino);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarEnsino(@RequestBody EnsinoDTO ensinoDTO)
    {
        return ensinoFacade.criarEnsino(ensinoDTO);
    }

    @Operation(summary = "Atualizar Ensino.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarEnsino(@RequestBody EnsinoDTO ensinoDTO)
    {
        return ensinoFacade.atualizarEnsino(ensinoDTO.getIdEnsino(), ensinoDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarEnsino(@RequestParam(value = "idEnsino") Long idEnsino)
    {
        return ensinoFacade.deletarEnsino(idEnsino);
    }

}



