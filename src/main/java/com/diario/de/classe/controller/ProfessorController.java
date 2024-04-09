package com.diario.de.classe.controller;

import com.diario.de.classe.facade.ProfessorFacade;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/professor")

public class ProfessorController extends DefaultController {
    final private static Logger LOG = LogManager.getLogger(ProfessorController.class);

    @Autowired
    ProfessorFacade professorFacade;

   @ApiOperation(value = "Buscar professor por IdProfessor", produces = MediaType.APPLICATION_JSON_VALUE)
   @GetMapping("/IdProfessor")
   public ResponseEntity<Object> buscarProfessorByCodPrf(@RequestParam (value = "IdProfessor") Long codPrf){
       return professorFacade.buscarProfessorByCodPrf(codPrf);
   }

    @ApiOperation(value = "Buscar todos os professores.", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("")
    public ResponseEntity<Object> buscarTodosProfessores()
    {
        return professorFacade.buscarTodosProfessores();
    }
}
