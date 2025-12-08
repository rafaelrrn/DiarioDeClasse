package com.diario.de.classe.controller;

import com.diario.de.classe.dto.CalendarioEscolarDTO;
import com.diario.de.classe.facade.CalendarioEscolarFacade;
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
@RequestMapping("/tabela-calendario-escolar")

public class CalendarioEscolarController {

    @Autowired
    private CalendarioEscolarFacade calendarioEscolarFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosCalendariosEscolares()
    {
       return calendarioEscolarFacade.buscarTodosCalendariosEscolares();
    }

    @Operation(summary = "Buscar Calendario Escolar por idCalendarioEscolar.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idCalendarioEscolar")
    public ResponseEntity<Object> buscarCalendarioEscolarPoridCalendarioEscolar(@RequestParam(value = "idCalendarioEscolar") Long idCalendarioEscolar)
    {
        return calendarioEscolarFacade.buscarCalendarioEscolarPoridCalendarioEscolar(idCalendarioEscolar);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarCalendarioEscolar(@RequestBody CalendarioEscolarDTO calendarioEscolarDTO)
    {
        return calendarioEscolarFacade.criarCalendarioEscolar(calendarioEscolarDTO);
    }

    @Operation(summary = "Atualizar Calendario Escolar.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarCalendarioEscolar(@RequestBody CalendarioEscolarDTO calendarioEscolarDTO)
    {
        return calendarioEscolarFacade.atualizarCalendarioEscolar(calendarioEscolarDTO.getIdCalendarioEscolar(), calendarioEscolarDTO);
    }

    @Operation(summary = "Excluir um registro do Calendario Escolar.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarCalendarioEscolar(@RequestParam(value = "idCalendarioEscolar") Long idCalendarioEscolar)
    {
        return calendarioEscolarFacade.deletarCalendarioEscolar(idCalendarioEscolar);
    }

}



