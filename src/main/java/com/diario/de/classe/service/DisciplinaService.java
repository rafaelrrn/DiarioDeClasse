package com.diario.de.classe.service;

import com.diario.de.classe.model.Disciplina;
import java.util.List;

public interface DisciplinaService {

    List<Disciplina> buscarTodasDisciplinas();

    Disciplina buscarDisciplinaPoridDisciplina(Long idDisciplina);

    Disciplina criarDisciplina(Disciplina disciplina);

    Disciplina atualizarDisciplina(Long idDisciplina, Disciplina disciplina, Disciplina disciplinaDoBanco);

    Disciplina deletarDisciplina(Disciplina disciplina);

} 