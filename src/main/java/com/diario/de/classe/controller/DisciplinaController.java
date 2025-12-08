package com.diario.de.classe.controller;

import com.diario.de.classe.facade.DisciplinaFacade;
import com.diario.de.classe.dto.DisciplinaDTO;
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
@RequestMapping("/tabela-de-disciplina")

public class DisciplinaController {

    @Autowired
    private DisciplinaFacade disciplinaFacade;

    @Operation(summary = "Buscar todas as Disciplinas.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodasDisciplinas()
    {
       return disciplinaFacade.buscarTodasDisciplinas();
    }

    @Operation(summary = "Buscar Disciplina por idDisciplina.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idDisciplina")
    public ResponseEntity<Object> buscarDisciplinaPoridDisciplina(@RequestParam(value = "idDisciplina") Long idDisciplina)
    {
        return disciplinaFacade.buscarDisciplinaPoridDisciplina(idDisciplina);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarDisciplina(@RequestBody DisciplinaDTO disciplinaDTO)
    {
        return disciplinaFacade.criarDisciplina(disciplinaDTO);
    }

    @Operation(summary = "Atualizar Disciplina.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarDisciplina(@RequestBody DisciplinaDTO disciplinaDTO)
    {
        return disciplinaFacade.atualizarDisciplina(disciplinaDTO.getIdDisciplina(), disciplinaDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarDisciplina(@RequestParam(value = "idDisciplina") Long idDisciplina)
    {
        return disciplinaFacade.deletarDisciplina(idDisciplina);
    }

}



