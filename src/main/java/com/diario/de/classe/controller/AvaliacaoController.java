package com.diario.de.classe.controller;

import com.diario.de.classe.dto.AvaliacaoDTO;
import com.diario.de.classe.facade.AvaliacaoFacade;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@RestController eu estou definindo o tipo de api de arquitetura RestFul
@RestController

//Permite qualquer ip acessar o end-point
@CrossOrigin

//Endereço do url para o end-point
@RequestMapping("/tabela-de-avaliacao")

public class AvaliacaoController {

    @Autowired
    private AvaliacaoFacade avaliacaoFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodoasAvaliacoes()
    {
       return avaliacaoFacade.buscarTodoasAvaliacoes();
    }

    @Operation(summary = "Buscar Avaliacao por idAvaliacao.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/buscar-avaliacao")
    public ResponseEntity<Object> buscarAvaliacaoPoridAvaliacao(@RequestParam(value = "idAvaliacao") Long idAvaliacao)
    {
        return avaliacaoFacade.buscarAvaliacaoPoridAvaliacao(idAvaliacao);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarAvaliacao(@RequestBody AvaliacaoDTO avaliacaoDTO)
    {
        return avaliacaoFacade.criarAvaliacao(avaliacaoDTO);
    }

    @Operation(summary = "Atualizar Avaliacao.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarAvaliacao(@RequestBody AvaliacaoDTO avaliacaoDTO) {
        return avaliacaoFacade.atualizarAvaliacao(avaliacaoDTO.getIdAvaliacao(), avaliacaoDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarAvaliacao(@RequestParam(value = "idAvaliacao") Long idAvaliacao)
    {
        return avaliacaoFacade.deletarAvaliacao(idAvaliacao);
    }



}



