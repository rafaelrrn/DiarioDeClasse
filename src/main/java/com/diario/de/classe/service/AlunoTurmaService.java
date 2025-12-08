package com.diario.de.classe.service;

import com.diario.de.classe.model.AlunoTurma;

import java.util.List;

public interface AlunoTurmaService {

    List<AlunoTurma> buscarTodosAlunosTurmas();

    AlunoTurma buscarAlunoTurmaPorIdAlunoTurma(Long idAlunoTurma);

    AlunoTurma criarAlunoTurma(AlunoTurma alunoTurma);

    AlunoTurma atualizarAlunoTurma(AlunoTurma alunoTurmaDoBanco);

    AlunoTurma deletarAlunoTurma(AlunoTurma alunoTurma);

}
