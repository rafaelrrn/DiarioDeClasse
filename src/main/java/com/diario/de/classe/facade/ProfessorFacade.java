package com.diario.de.classe.facade;

import org.springframework.http.ResponseEntity;

public interface ProfessorFacade {
    ResponseEntity<Object> buscarProfessorByCodPrf(Long codPrf);
    ResponseEntity<Object> buscarTodosProfessores();
}
