package com.diario.de.classe.controller;

import com.diario.de.classe.dto.EnderecoDTO;
import com.diario.de.classe.facade.EnderecoFacade;
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
@RequestMapping("/tabela-de-endereco")

public class EnderecoController {

    @Autowired
    private EnderecoFacade enderecoFacade;

    @Operation(summary = "Buscar todos os Enderecos.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosEnderecos()
    {
       return enderecoFacade.buscarTodosEnderecos();
    }

    @Operation(summary = "Buscar Endereco por idEndereco.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idEndereco")
    public ResponseEntity<Object> buscarEnderecoPoridEndereco(@RequestParam(value = "idEndereco") Long idEndereco)
    {
        return enderecoFacade.buscarEnderecoPoridEndereco(idEndereco);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarEndereco(@RequestBody EnderecoDTO enderecoDTO)
    {
        return enderecoFacade.criarEndereco(enderecoDTO);
    }

    @Operation(summary = "Atualizar Endereco.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarEndereco(@RequestBody EnderecoDTO enderecoDTO)
    {
        return enderecoFacade.atualizarEndereco(enderecoDTO.getIdEndereco(), enderecoDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarEndereco(@RequestParam(value = "idEndereco") Long idEndereco)
    {
        return enderecoFacade.deletarEndereco(idEndereco);
    }

}



