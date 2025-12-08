package com.diario.de.classe.controller;

import com.diario.de.classe.facade.InstituicaoEnsinoFacade;
import com.diario.de.classe.dto.InstituicaoEnsinoDTO;
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
@RequestMapping("/tabela-de-instituicao-ensino")

public class InstituicaoEnsinoController {

    @Autowired
    private InstituicaoEnsinoFacade instituicaoEnsinoFacade;

    @Operation(summary = "Buscar todas as Instituições Ensino.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodasInstituicoesEnsino()
    {
       return instituicaoEnsinoFacade.buscarTodasInstituicoesEnsino();
    }

    @Operation(summary = "Buscar Instituição de Ensino por idInstituicaoEnsino.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idInstituicaoEnsino")
    public ResponseEntity<Object> buscarInstituicaoEnsinoPoridInstituicaoEnsino(@RequestParam(value = "idInstituicaoEnsino") Long idInstituicaoEnsino)
    {
        return instituicaoEnsinoFacade.buscarInstituicaoEnsinoPoridInstituicaoEnsino(idInstituicaoEnsino);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarInstituicaoEnsino(@RequestBody InstituicaoEnsinoDTO instituicaoEnsinoDTO)
    {
        return instituicaoEnsinoFacade.criarInstituicaoEnsino(instituicaoEnsinoDTO);
    }

    @Operation(summary = "Atualizar Instituicao de Ensino.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarInstituicaoEnsino(@RequestBody InstituicaoEnsinoDTO instituicaoEnsinoDTO)
    {
        return instituicaoEnsinoFacade.atualizarInstituicaoEnsino(instituicaoEnsinoDTO.getIdInstituicaoEnsino(), instituicaoEnsinoDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarInstituicaoEnsino(@RequestParam(value = "idInstituicaoEnsino") Long idInstituicaoEnsino)
    {
        return instituicaoEnsinoFacade.deletarInstituicaoEnsino(idInstituicaoEnsino);
    }

}



