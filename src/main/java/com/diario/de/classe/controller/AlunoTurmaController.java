package com.diario.de.classe.controller;

import com.diario.de.classe.dto.AlunoTurmaDTO;
import com.diario.de.classe.facade.AlunoTurmaFacade;
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
@RequestMapping("/tabela-aluno-turma")

public class AlunoTurmaController {

    @Autowired
    private AlunoTurmaFacade alunoTurmaFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosAlunosTurmas() {
        return alunoTurmaFacade.buscarTodosAlunosTurmas();
    }


    @Operation(summary = "Buscar AlunoTurma por IdAlunoTurma.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/buscar-aluno-turma")
    public ResponseEntity<Object> buscarAlunoTurmaPorIdAlunoTurma(@RequestParam(value = "IdAlunoTurma") Long idAlunoTurma) {
        return alunoTurmaFacade.buscarAlunoTurmaPorIdAlunoTurma(idAlunoTurma);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarAlunoTurma(@RequestBody AlunoTurmaDTO alunoTurmaDTO) {
        return alunoTurmaFacade.criarAlunoTurma(alunoTurmaDTO);
    }

    @Operation(summary = "Atualizar AlunoTurma.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarAlunoTurma(@RequestBody AlunoTurmaDTO alunoTurmaDTO) {
        return alunoTurmaFacade.atualizarAlunoTurma(alunoTurmaDTO.getIdAlunoTurma(), alunoTurmaDTO);
    }

    @Operation(summary = "Excluir um registro do AlunoTurma.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarAlunoTurma(@RequestParam(value = "idAlunoTurma") Long idAlunoTurma) {
        return alunoTurmaFacade.deletarAlunoTurma(idAlunoTurma);
    }
}






