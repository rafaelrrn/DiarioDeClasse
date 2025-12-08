package com.diario.de.classe.service.old;

import com.diario.de.classe.model.old.Professor;

import java.util.List;

public interface ProfessorService {

    List<Professor> buscarTodosProfessores();

    Professor buscarProfessorByCodPrf(Long codPrf);

    Professor criarProfessor(Professor professor);

    Professor atualizarProfessor(Long codPrf, String professorBody, Professor professorDoBanco);

    Professor deletarProfessor(Professor professor);


}
