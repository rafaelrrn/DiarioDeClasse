package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.facade.ProfessorFacade;
import com.diario.de.classe.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfessorFacadeImpl implements ProfessorFacade {
    @Autowired
    ProfessorService professorService;

    @Override
    public ResponseEntity<Object> buscarTodosProfessores() {
        return  professorService.buscarTodosProfessores();
    }

    @Override
    public ResponseEntity<Object> buscarProfessorByCodPrf(Long codPrf){
        return professorService.buscarProfessorByCodPrf(codPrf);
    }

}
