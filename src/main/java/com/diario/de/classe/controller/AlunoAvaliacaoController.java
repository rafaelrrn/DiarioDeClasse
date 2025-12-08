package com.diario.de.classe.controller;

import com.diario.de.classe.dto.AlunoAvaliacaoDTO;
import com.diario.de.classe.facade.AlunoAvaliacaoFacade;
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
@RequestMapping("/tabela-aluno-avaliacao")

public class AlunoAvaliacaoController {

    @Autowired
    private AlunoAvaliacaoFacade alunoAvaliacaoFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosAlunosAvaliacoes() {
        return alunoAvaliacaoFacade.buscarTodosAlunosAvaliacoes();
    }

    @Operation(summary = "Buscar AlunoAvaliacao por IdAlunoAvaliacao.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/buscar-aluno-avaliacao")
    public ResponseEntity<Object> buscarAlunoAvaliacaoPorIdAlunoAvaliacao(@RequestParam(value = "idAlunoAvaliacao") Long idAlunoAvaliacao) {
        return alunoAvaliacaoFacade.buscarAlunoAvaliacaoPorIdAlunoAvaliacao(idAlunoAvaliacao);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarAlunoAvaliacao(@RequestBody AlunoAvaliacaoDTO alunoAvaliacaoDTO) {
        return alunoAvaliacaoFacade.criarAlunoAvaliacao(alunoAvaliacaoDTO);
    }

    @Operation(summary = "Atualizar AlunoAvaliacao.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarAlunoAvaliacao(@RequestBody AlunoAvaliacaoDTO alunoAvaliacaoDTO) {
        return alunoAvaliacaoFacade.atualizarAlunoAvaliacao(alunoAvaliacaoDTO.getIdAlunoAvaliacao(), alunoAvaliacaoDTO);
    }

    @Operation(summary = "Excluir um registro do AlunoAvaliacao.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarAlunoAvaliacao(@RequestParam(value = "idAlunoAvaliacao") Long idAlunoAvaliacao) {
        return alunoAvaliacaoFacade.deletarAlunoAvaliacao(idAlunoAvaliacao);
    }
}






