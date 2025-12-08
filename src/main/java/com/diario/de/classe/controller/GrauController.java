package com.diario.de.classe.controller;

import com.diario.de.classe.dto.GrauDTO;
import com.diario.de.classe.facade.GrauFacade;
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
@RequestMapping("/tabela-de-grau")

public class GrauController {

    @Autowired
    private GrauFacade grauFacade;

    @Operation(summary = "Buscar todos os Graus.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosGraus()
    {
       return grauFacade.buscarTodosGraus();
    }

    @Operation(summary = "Buscar Grau por idGrau.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idGrau")
    public ResponseEntity<Object> buscarGrauPoridGrau(@RequestParam(value = "idGrau") Long idGrau)
    {
        return grauFacade.buscarGrauPoridGrau(idGrau);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarGrau(@RequestBody GrauDTO grauDTO)
    {
        return grauFacade.criarGrau(grauDTO);
    }

    @Operation(summary = "Atualizar o Grau.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarGrau(@RequestBody GrauDTO grauDTO)
    {
        return grauFacade.atualizarGrau(grauDTO.getIdGrau(), grauDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarGrau(@RequestParam(value = "idGrau") Long idGrau)
    {
        return grauFacade.deletarGrau(idGrau);
    }

}



