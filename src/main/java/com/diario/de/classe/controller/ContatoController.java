package com.diario.de.classe.controller;

import com.diario.de.classe.dto.ContatoDTO;
import com.diario.de.classe.facade.ContatoFacade;
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
@RequestMapping("/tabela-de-contato")

public class ContatoController {

    @Autowired
    private ContatoFacade contatoFacade;

    @Operation(summary = "Buscar todos os registros.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosContatos()
    {
       return contatoFacade.buscarTodosContatos();
    }

    @Operation(summary = "Buscar Contato por idContato.")
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/idContato")
    public ResponseEntity<Object> buscarContatoPoridContato(@RequestParam(value = "idContato") Long idContato)
    {
        return contatoFacade.buscarContatoPoridContato(idContato);
    }

    @Operation(summary = "Inserir um novo registro.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("")
    public ResponseEntity<Object> criarContato(@RequestBody ContatoDTO contatoDTO)
    {
        return contatoFacade.criarContato(contatoDTO);
    }

    @Operation(summary = "Atualizar Contato.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("")
    public ResponseEntity<Object> atualizarContato(@RequestBody ContatoDTO contatoDTO)
    {
        return contatoFacade.atualizarContato(contatoDTO.getIdContato(), contatoDTO);
    }

    @Operation(summary = "Excluir um registro do Contato.")
    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("")
    public ResponseEntity<Object> deletarCurso(@RequestParam(value = "idContato") Long idContato)
    {
        return contatoFacade.deletarContato(idContato);
    }



}



