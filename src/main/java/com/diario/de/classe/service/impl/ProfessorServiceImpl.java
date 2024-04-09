package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Professor;
import com.diario.de.classe.repository.jpa.ProfessorRepositoryJpa;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;

    @Autowired
    ProfessorRepositoryJpa professorRepositoryJpa;

    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;

    @Override
    public ResponseEntity<Object> buscarTodosProfessores() {
        List<Professor> allProfessors = professorRepositoryJpa.findAll();

        if (allProfessors.isEmpty()) return responseDiarioDeClasse.error(NOT_FOUND_MSG, HttpStatus.NOT_FOUND);
        return responseDiarioDeClasse.success(allProfessors, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> buscarProfessorByCodPrf(Long codPrf){
        Optional<Professor> professor = professorRepositoryJpa.findById(codPrf);

        if (professor.isEmpty())  return responseDiarioDeClasse.error(NOT_FOUND_MSG, HttpStatus.NOT_FOUND);
        return responseDiarioDeClasse.success(professor, HttpStatus.OK);
    }

}
