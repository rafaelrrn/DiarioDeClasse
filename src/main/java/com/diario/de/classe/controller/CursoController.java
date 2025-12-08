package com.diario.de.classe.controller;

import com.diario.de.classe.dto.CursoDTO;
import com.diario.de.classe.facade.CursoFacade;
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
@RequestMapping("/tabela-de-curso")

public class CursoController {

    @Autowired
    private CursoFacade cursoFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosCursos()
    {
       return cursoFacade.buscarTodosCursos();
    }

    @Operation(summary = "Buscar Curso por idCurso.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idCurso")
    public ResponseEntity<Object> buscarCursoPoridCurso(@RequestParam(value = "idCurso") Long idCurso)
    {
        return cursoFacade.buscarCursoPoridCurso(idCurso);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarCurso(@RequestBody CursoDTO cursoDTO)
    {
        return cursoFacade.criarCurso(cursoDTO);
    }

    @Operation(summary = "Atualizar Curso.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarCurso(@RequestBody CursoDTO cursoDTO)
    {
        return cursoFacade.atualizarCurso(cursoDTO.getIdCurso(), cursoDTO);
    }

    @Operation(summary = "Excluir um registro do Curso.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarCurso(@RequestParam(value = "idCurso") Long idCurso)
    {
        return cursoFacade.deletarCurso(idCurso);
    }



}



