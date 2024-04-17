package com.diario.de.classe.controller;

import com.diario.de.classe.facade.ClasseEscolarFacade;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/tabela-de-classe")
public class ClasseEscolarController extends DefaultController {

    @Autowired
    ClasseEscolarFacade classeEscolarFacade;

    @ApiOperation(value = "Buscar todas as classes.", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("")
    public ResponseEntity<Object> buscarTodasClasses()
    {
        return classeEscolarFacade.buscarTodasClasses();
    }

    @ApiOperation(value = "Buscar classe por ID.", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/IdClasseEscolar")
    public ResponseEntity<Object> buscarClassePorCodCls(@RequestParam (value = "IdClasseEscolar") Long codCls)
    {
        return classeEscolarFacade.buscarClassePorCodCls(codCls);
    }

    @ApiOperation(value = "Inserir uma nova classe.", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("")
    public ResponseEntity<Object> criarClasse(@RequestBody String classe)
    {
        return classeEscolarFacade.criarClasse(classe);
    }

    @ApiOperation(value = "Atualizar uma classe.", produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping("")
    public ResponseEntity<Object> atualizarClasse(@RequestParam(value = "IdClasse") Long codCls, @RequestBody String classeEscolarBody)
    {
        return classeEscolarFacade.atualizarClasse(codCls, classeEscolarBody);
    }

    @ApiOperation(value = "Excluir uma classe.", produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping("")
    public ResponseEntity<Object> deletarClasse(@RequestParam(value = "IdClasse") Long codCls)
    {
        return classeEscolarFacade.deletarClasse(codCls);
    }
}
