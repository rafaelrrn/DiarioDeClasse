package com.diario.de.classe.controller;

import com.diario.de.classe.dto.PessoaResponsavelDTO;
import com.diario.de.classe.facade.PessoaResponsavelFacade;
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
@RequestMapping("/tabela-de-pessoa-responsavel")

public class PessoaResponsavelController {

    @Autowired
    private PessoaResponsavelFacade pessoaResponsavelFacade;

    @Operation(summary = "Buscar todas as Pessoas Responsaveis.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodasPessoasResponsaveis()
    {
       return pessoaResponsavelFacade.buscarTodasPessoasResponsaveis();
    }

    @Operation(summary = "Buscar Pessoa Responsavel por idPessoa.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idPessoaResponsavel")
    public ResponseEntity<Object> buscarPessoaResponsavelPoridPessoaResponsavel(@RequestParam(value = "idPessoaResponsavel") Long idPessoaResponsavel)
    {
        return pessoaResponsavelFacade.buscarPessoaResponsavelPoridPessoa(idPessoaResponsavel);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarPessoaResponsavel(@RequestBody PessoaResponsavelDTO pessoaResponsavelDTO)
    {
        return pessoaResponsavelFacade.criarPessoaResponsavel(pessoaResponsavelDTO);
    }

    @Operation(summary = "Atualizar Pessoa Responsavel.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarPessoaResponsavel(@RequestBody PessoaResponsavelDTO pessoaResponsavelDTO)
    {
        return pessoaResponsavelFacade.atualizarPessoaResponsavel(pessoaResponsavelDTO.getIdPessoaResponsavel(), pessoaResponsavelDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarPessoaResponsavel(@RequestParam(value = "idPessoaResponsavel") Long idPessoaResponsavel)
    {
        return pessoaResponsavelFacade.deletarPessoaResponsavel(idPessoaResponsavel);
    }

}



