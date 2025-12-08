package com.diario.de.classe.service;

import com.diario.de.classe.model.Curso;
import java.util.List;

public interface CursoService {

    List<Curso> buscarTodosCursos();

    Curso buscarCursoPoridCurso(Long idCurso);

    Curso criarCurso(Curso curso);

    Curso atualizarCurso(Curso cursoDoBanco);

    Curso deletarCurso(Curso curso);

} 