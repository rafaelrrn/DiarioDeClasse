package com.diario.de.classe.controller;

import com.diario.de.classe.dto.ClasseDTO;
import com.diario.de.classe.facade.ClasseFacade;
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
@RequestMapping("/tabela-de-classe")

public class ClasseController {

    @Autowired
    private ClasseFacade classeFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodasClasses()
    {
       return classeFacade.buscarTodasClasses();
    }

    @Operation(summary = "Buscar Classe por idClasse.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idClasse")
    public ResponseEntity<Object> buscarClassePoridClasse(@RequestParam(value = "idClasse") Long idClasse)
    {
        return classeFacade.buscarClassePoridClasse(idClasse);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarClasse(@RequestBody ClasseDTO classeDTO)
    {
        return classeFacade.criarClasse(classeDTO);
    }

    @Operation(summary = "Atualizar Classe.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarClasse(@RequestBody ClasseDTO classeDTO)
    {
        return classeFacade.atualizarClasse(classeDTO.getIdClasse(), classeDTO);
    }

    @Operation(summary = "Excluir um registro da Classe.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarClasse(@RequestParam(value = "idClasse") Long idClasse)
    {
        return classeFacade.deletarClasse(idClasse);
    }



}



