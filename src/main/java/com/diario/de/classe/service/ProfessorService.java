package com.diario.de.classe.service;

import org.springframework.http.ResponseEntity;

public interface ProfessorService {

    ResponseEntity<Object> buscarTodosProfessores();
    ResponseEntity<Object> buscarProfessorByCodPrf(Long codPrf);
}
