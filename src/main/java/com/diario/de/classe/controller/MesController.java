package com.diario.de.classe.controller;

import com.diario.de.classe.dto.MesDTO;
import com.diario.de.classe.facade.MesFacade;
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
@RequestMapping("/tabela-de-mes")

public class MesController {

    @Autowired
    private MesFacade mesFacade;

    @Operation(summary = "Buscar todos os Meses.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosMeses()
    {
       return mesFacade.buscarTodosMeses();
    }

    @Operation(summary = "Buscar Mes por idMes.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idMes")
    public ResponseEntity<Object> buscarMesPoridMes(@RequestParam(value = "idMes") Long idMes)
    {
        return mesFacade.buscarMesPoridMes(idMes);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarMes(@RequestBody MesDTO mesDTO)
    {
        return mesFacade.criarMes(mesDTO);
    }

    @Operation(summary = "Atualizar Mes.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarMes(@RequestBody MesDTO mesDTO)
    {
        return mesFacade.atualizarMes(mesDTO.getIdMes(), mesDTO);
    }

    @Operation(summary = "Excluir um registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarMes(@RequestParam(value = "idMes") Long idMes)
    {
        return mesFacade.deletarMes(idMes);
    }

}



