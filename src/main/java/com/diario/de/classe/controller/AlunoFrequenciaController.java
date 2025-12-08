package com.diario.de.classe.controller;

import com.diario.de.classe.dto.AlunoFrequenciaDTO;
import com.diario.de.classe.facade.AlunoFrequenciaFacade;
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
@RequestMapping("/tabela-aluno-frequencia")

public class AlunoFrequenciaController {

    @Autowired
    private AlunoFrequenciaFacade alunoFrequenciaFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosAlunosFrequencias() {
        return alunoFrequenciaFacade.buscarTodosAlunosFrequencias();
    }

    @Operation(summary = "Buscar AlunoFrequencia por IdAlunoFrequencia.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/buscar-aluno-frequencia")
    public ResponseEntity<Object> buscarAlunoFrequenciaPorIdAlunoFrequencia(@RequestParam(value = "idAlunoFrequencia") Long idAlunoFrequencia) {
        return alunoFrequenciaFacade.buscarAlunoFrequenciaPorIdAlunoFrequencia(idAlunoFrequencia);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarAlunoFrequencia(@RequestBody AlunoFrequenciaDTO alunoFrequenciaDTO) {
        return alunoFrequenciaFacade.criarAlunoFrequencia(alunoFrequenciaDTO);
    }

    @Operation(summary = "Atualizar AlunoFrequencia.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarAlunoFrequencia(@RequestBody AlunoFrequenciaDTO alunoFrequenciaDTO) {
        return alunoFrequenciaFacade.atualizarAlunoFrequencia(alunoFrequenciaDTO.getIdAlunoFrequencia(), alunoFrequenciaDTO);
    }

    @Operation(summary = "Excluir um registro do AlunoFrequencia.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarAlunoFrequencia(@RequestParam(value = "idAlunoFrequencia") Long idAlunoFrequencia) {
        return alunoFrequenciaFacade.deletarAlunoFrequencia(idAlunoFrequencia);
    }
}






