package com.diario.de.classe.controller;

import com.diario.de.classe.dto.TipoPessoaDTO;
import com.diario.de.classe.facade.TipoPessoaFacade;
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
@RequestMapping("/tabela-de-tipo-pessoa")

public class TipoPessoaController {

    @Autowired
    private TipoPessoaFacade tipoPessoaFacade;

    @Operation(summary = "Buscar todos os Tipo Pessoas.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosTipoPessoas()
    {
       return tipoPessoaFacade.buscarTodosTipoPessoas();
    }

    @Operation(summary = "Buscar Tipo Pessoa por idTipoPessoa.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idTipoPessoa")
    public ResponseEntity<Object> buscarTipoPessoaPoridTipoPessoa(@RequestParam(value = "idTipoPessoa") Long idTipoPessoa)
    {
        return tipoPessoaFacade.buscarTipoPessoaPoridTipoPessoa(idTipoPessoa);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarTipoPessoa(@RequestBody TipoPessoaDTO tipoPessoaDTO)
    {
        return tipoPessoaFacade.criarTipoPessoa(tipoPessoaDTO);
    }

    @Operation(summary = "Atualizar Tipo Pessoa.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarTipoPessoa(@RequestBody TipoPessoaDTO tipoPessoaDTO)
    {
        return tipoPessoaFacade.atualizarTipoPessoa(tipoPessoaDTO.getIdTipoPessoa(), tipoPessoaDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarTipoPessoa(@RequestParam(value = "idTipoPessoa") Long idTipoPessoa)
    {
        return tipoPessoaFacade.deletarTipoPessoa(idTipoPessoa);
    }

}



