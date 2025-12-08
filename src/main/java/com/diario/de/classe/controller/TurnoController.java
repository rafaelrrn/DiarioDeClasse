package com.diario.de.classe.controller;

import com.diario.de.classe.dto.TurnoDTO;
import com.diario.de.classe.facade.TurnoFacade;
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
@RequestMapping("/tabela-de-turno")

public class TurnoController {

    @Autowired
    private TurnoFacade turnoFacade;

    @Operation(summary = "Buscar todos os Turnos.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosTurnos()
    {
       return turnoFacade.buscarTodosTurnos();
    }

    @Operation(summary = "Buscar Turno por idTurno.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idTurno")
    public ResponseEntity<Object> buscarTurnoPoridTurno(@RequestParam(value = "idTurno") Long idTurno)
    {
        return turnoFacade.buscarTurnoPoridTurno(idTurno);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarTurno(@RequestBody TurnoDTO turnoDTO)
    {
        return turnoFacade.criarTurno(turnoDTO);
    }

    @Operation(summary = "Atualizar Turno.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarTurno(@RequestBody TurnoDTO turnoDTO)
    {
        return turnoFacade.atualizarTurno(turnoDTO.getIdTurno(), turnoDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarTurno(@RequestParam(value = "idTurno") Long idTurno)
    {
        return turnoFacade.deletarTurno(idTurno);
    }

}



