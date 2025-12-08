package com.diario.de.classe.controller;

import com.diario.de.classe.dto.ComponenteCurricularDTO;
import com.diario.de.classe.facade.ComponenteCurricularFacade;
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
@RequestMapping("/tabela-de-componente-curricular")

public class ComponenteCurricularController {

    @Autowired
    private ComponenteCurricularFacade componenteCurricularFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosComponentesCurriculares()
    {
       return componenteCurricularFacade.buscarTodosComponentesCurriculares();
    }

    @Operation(summary = "Buscar Componente Curricular por idComponenteCurricular.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idComponenteCurricular")
    public ResponseEntity<Object> buscarComponenteCurricularPoridComponenteCurricular(@RequestParam(value = "idComponenteCurricular") Long idComponenteCurricular)
    {
        return componenteCurricularFacade.buscarComponenteCurricularPoridComponenteCurricular(idComponenteCurricular);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarComponenteCurricular(@RequestBody ComponenteCurricularDTO componenteCurricularDTO)
    {
        return componenteCurricularFacade.criarComponenteCurricular(componenteCurricularDTO);
    }

    @Operation(summary = "Atualizar Componente Curricular.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarComponenteCurricular(@RequestBody ComponenteCurricularDTO componenteCurricularDTO)
    {
        return componenteCurricularFacade.atualizarComponenteCurricular(componenteCurricularDTO.getIdComponenteCurricular(), componenteCurricularDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarComponenteCurricular(@RequestParam(value = "idComponenteCurricular") Long idComponenteCurricular)
    {
        return componenteCurricularFacade.deletarComponenteCurricular(idComponenteCurricular);
    }



}



