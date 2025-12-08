package com.diario.de.classe.controller;

import com.diario.de.classe.dto.ContatoPessoaDTO;
import com.diario.de.classe.facade.ContatoPessoaFacade;
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
@RequestMapping("/tabela-de-contato-pessoa")

public class ContatoPessoaController {

    @Autowired
    private ContatoPessoaFacade contatoPessoaFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodasPessoasContatos()
    {
       return contatoPessoaFacade.buscarTodasPessoasContatos();
    }

    @Operation(summary = "Buscar Contato por idContatoPessoa.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idContatoPessoa")
    public ResponseEntity<Object> buscarContatoPessoaPoridContatoPessoa(@RequestParam(value = "idContatoPessoa") Long idContatoPessoa)
    {
        return contatoPessoaFacade.buscarContatoPessoaPoridContatoPessoa(idContatoPessoa);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarContatoPessoa(@RequestBody ContatoPessoaDTO contatoPessoaDTO)
    {
        return contatoPessoaFacade.criarContatoPessoa(contatoPessoaDTO);
    }

    @Operation(summary = "Atualizar Contato Pessoa.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarContatoPessoa(@RequestBody ContatoPessoaDTO contatoPessoaDTO)
    {
        return contatoPessoaFacade.atualizarContatoPessoa(contatoPessoaDTO.getIdContatoPessoa(), contatoPessoaDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarContatoPessoa(@RequestParam(value = "idContatoPessoa") Long idContatoPessoa)
    {
        return contatoPessoaFacade.deletarContatoPessoa(idContatoPessoa);
    }



}



