package com.diario.de.classe.controller;

import com.diario.de.classe.dto.PeriodoDTO;
import com.diario.de.classe.facade.PeriodoFacade;
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
@RequestMapping("/tabela-de-periodo")

public class PeriodoController {

    @Autowired
    private PeriodoFacade periodoFacade;

    @Operation(summary = "Buscar todos os Periodos.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosPeriodos()
    {
       return periodoFacade.buscarTodosPeriodos();
    }

    @Operation(summary = "Buscar Periodo por idPeriodo.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idPeriodo")
    public ResponseEntity<Object> buscarPeriodoPoridPeriodo(@RequestParam(value = "idPeriodo") Long idPeriodo)
    {
        return periodoFacade.buscarPeriodoPoridPeriodo(idPeriodo);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarPeriodo(@RequestBody PeriodoDTO periodoDTO)
    {
        return periodoFacade.criarPeriodo(periodoDTO);
    }

    @Operation(summary = "Atualizar Periodo.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarPeriodo(@RequestBody PeriodoDTO periodoDTO)
    {
        return periodoFacade.atualizarPeriodo(periodoDTO.getIdPeriodo(), periodoDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarPeriodo(@RequestParam(value = "idPeriodo") Long idPeriodo)
    {
        return periodoFacade.deletarPeriodo(idPeriodo);
    }

}



