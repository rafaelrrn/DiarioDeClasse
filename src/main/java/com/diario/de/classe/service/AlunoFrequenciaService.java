package com.diario.de.classe.service;

import com.diario.de.classe.model.AlunoFrequencia;

import java.util.List;

public interface AlunoFrequenciaService {

    List<AlunoFrequencia> buscarTodosAlunosFrequencias();

    AlunoFrequencia buscarAlunoFrequenciaPorIdAlunoFrequencia(Long IdAlunoFrequencia);

    AlunoFrequencia criarAlunoFrequencia(AlunoFrequencia alunoFrequencia);

    AlunoFrequencia atualizarAlunoFrequencia(AlunoFrequencia alunoFrequenciaDoBanco);

    AlunoFrequencia deletarAlunoFrequencia(AlunoFrequencia alunoFrequencia);

}
