package com.diario.de.classe.controller;

import com.diario.de.classe.dto.AnoCalendarioDTO;
import com.diario.de.classe.facade.AnoCalendarioFacade;
import com.diario.de.classe.model.AnoCalendario;
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
@RequestMapping("/tabela-ano-calendario")

public class AnoCalendarioController {

    @Autowired
    private AnoCalendarioFacade anoCalendarioFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosAnosCalendarios()
    {
       return anoCalendarioFacade.buscarTodosAnosCalendarios();
    }

    @Operation(summary = "Buscar Ano Calendário por idAnoCalendario.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/buscar-ano-calendario")
    public ResponseEntity<Object> buscarAnoCalendarioPoridAnoCalendario(@RequestParam(value = "idAnoCalendario") Long idAnoCalendario)
    {
        return anoCalendarioFacade.buscarAnoCalendarioPoridAnoCalendario(idAnoCalendario);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarAnoCalendario(@RequestBody AnoCalendarioDTO anoCalendarioDTO)
    {
        return anoCalendarioFacade.criarAnoCalendario(anoCalendarioDTO);
    }

    @Operation(summary = "Atualizar Ano Calendario.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarAnoCalendario(@RequestBody AnoCalendarioDTO anoCalendarioDTO)
    {
        return anoCalendarioFacade.atualizarAnoCalendario(anoCalendarioDTO.getIdAnoCalendario(), anoCalendarioDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarAnoCalendario(@RequestParam(value = "idAnoCalendario") Long idAnoCalendario)
    {
        return anoCalendarioFacade.deletarAnoCalendario(idAnoCalendario);
    }



}



