package com.diario.de.classe.controller;

import com.diario.de.classe.dto.EnderecoPessoaDTO;
import com.diario.de.classe.facade.EnderecoPessoaFacade;
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
@RequestMapping("/tabela-de-endereco-pessoa")

public class EnderecoPessoaController {

    @Autowired
    private EnderecoPessoaFacade enderecoPessoaFacade;

    @Operation(summary = "Buscar todos os Enderecos Pessoas.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosEnderecoPessoas()
    {
       return enderecoPessoaFacade.buscarTodosEnderecoPessoas();
    }

    @Operation(summary = "Buscar Endereco Pessoa por idEnderecoPessoa.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idEnderecoPessoa")
    public ResponseEntity<Object> buscarEnderecoPessoaPorIdEnderecoPessoa(@RequestParam(value = "idEnderecoPessoa") Long idEnderecoPessoa)
    {
        return enderecoPessoaFacade.buscarEnderecoPessoaPorIdEnderecoPessoa(idEnderecoPessoa);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarEnderecoPessoa(@RequestBody EnderecoPessoaDTO enderecoPessoaDTO)
    {
        return enderecoPessoaFacade.criarEnderecoPessoa(enderecoPessoaDTO);
    }

    @Operation(summary = "Atualizar Endereco.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarEnderecoPessoa(@RequestBody EnderecoPessoaDTO enderecoPessoaDTO)
    {
        return enderecoPessoaFacade.atualizarEnderecoPessoa(enderecoPessoaDTO.getIdEnderecoPessoa(), enderecoPessoaDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarEndereco(@RequestParam(value = "idEnderecoPessoa") Long idEnderecoPessoa)
    {
        return enderecoPessoaFacade.deletarEnderecoPessoa(idEnderecoPessoa);
    }

}



