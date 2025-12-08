package com.diario.de.classe.controller;

import com.diario.de.classe.dto.TurmaDTO;
import com.diario.de.classe.facade.TurmaFacade;
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
@RequestMapping("/tabela-de-turma")

public class TurmaController {

    @Autowired
    private TurmaFacade turmaFacade;

    @Operation(summary = "Buscar todas as Turmas.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodasTurmas()
    {
       return turmaFacade.buscarTodasTurmas();
    }

    @Operation(summary = "Buscar Turma por idTurma.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idTurma")
    public ResponseEntity<Object> buscarTurmaPoridTurma(@RequestParam(value = "idTurma") Long idTurma)
    {
        return turmaFacade.buscarTurmaPoridTurma(idTurma);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarTurma(@RequestBody TurmaDTO turmaDTO)
    {
        return turmaFacade.criarTurma(turmaDTO);
    }

    @Operation(summary = "Atualizar Turma.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarTurma(@RequestBody TurmaDTO turmaDTO)
    {
        return turmaFacade.atualizarTurma(turmaDTO.getIdTurma(), turmaDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarTurma(@RequestParam(value = "idTurma") Long idTurma)
    {
        return turmaFacade.deletarTurma(idTurma);
    }

}



