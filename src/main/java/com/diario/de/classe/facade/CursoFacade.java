package com.diario.de.classe.facade;

import com.diario.de.classe.dto.CursoDTO;
import org.springframework.http.ResponseEntity;

public interface CursoFacade {
    ResponseEntity<Object> buscarTodosCursos();

    ResponseEntity<Object> buscarCursoPoridCurso(Long idCurso);

    ResponseEntity<Object> criarCurso(CursoDTO cursoDTO);

    ResponseEntity<Object> atualizarCurso(Long idCurso, CursoDTO cursoDTO);

    ResponseEntity<Object> deletarCurso(Long idCurso);

}
