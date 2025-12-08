package com.diario.de.classe.service;

import com.diario.de.classe.model.Turma;
import java.util.List;

public interface TurmaService {

    List<Turma> buscarTodasTurmas();

    Turma buscarTurmaPoridTurma(Long idTurma);

    Turma criarTurma(Turma turma);

    Turma atualizarTurma(Long idTurma, Turma turma, Turma turmaDoBanco);

    Turma deletarTurma(Turma turma);

} 