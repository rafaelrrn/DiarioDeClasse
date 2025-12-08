package com.diario.de.classe.controller;

import com.diario.de.classe.dto.SerieDTO;
import com.diario.de.classe.facade.SerieFacade;
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
@RequestMapping("/tabela-de-serie")

public class SerieController {

    @Autowired
    private SerieFacade serieFacade;

    @Operation(summary = "Buscar todas as Series.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodasSeries()
    {
       return serieFacade.buscarTodasSeries();
    }

    @Operation(summary = "Buscar Serie por idSerie.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idSerie")
    public ResponseEntity<Object> buscarSeriePoridSerie(@RequestParam(value = "idSerie") Long idSerie)
    {
        return serieFacade.buscarSeriePoridSerie(idSerie);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarSerie(@RequestBody SerieDTO serieDTO)
    {
        return serieFacade.criarSerie(serieDTO);
    }

    @Operation(summary = "Atualizar Serie.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarSerie(@RequestBody SerieDTO serieDTO)
    {
        return serieFacade.atualizarSerie(serieDTO.getIdSerie(), serieDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarSerie(@RequestParam(value = "idSerie") Long idSerie)
    {
        return serieFacade.deletarSerie(idSerie);
    }

}



