package com.diario.de.classe.controller;

import com.diario.de.classe.dto.PessoaDTO;
import com.diario.de.classe.facade.PessoaFacade;
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
@RequestMapping("/tabela-de-pessoa")

public class PessoaController {

    @Autowired
    private PessoaFacade pessoaFacade;

    @Operation(summary = "Buscar todas as Pessoas.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodasPessoas()
    {
       return pessoaFacade.buscarTodasPessoas();
    }

    @Operation(summary = "Buscar Pessoa por idPessoa.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idPessoa")
    public ResponseEntity<Object> buscarPessoaPoridPessoa(@RequestParam(value = "idPessoa") Long idPessoa)
    {
        return pessoaFacade.buscarPessoaPoridPessoa(idPessoa);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarPessoa(@RequestBody PessoaDTO pessoaDTO)
    {
        return pessoaFacade.criarPessoa(pessoaDTO);
    }

    @Operation(summary = "Atualizar Pessoa.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarPessoa(@RequestBody PessoaDTO pessoaDTO)
    {
        return pessoaFacade.atualizarPessoa(pessoaDTO.getIdPessoa(), pessoaDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarPessoa(@RequestParam(value = "idPessoa") Long idPessoa)
    {
        return pessoaFacade.deletarPessoa(idPessoa);
    }

}



