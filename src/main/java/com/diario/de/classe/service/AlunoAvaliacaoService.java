package com.diario.de.classe.service;

import com.diario.de.classe.model.AlunoAvaliacao;


import java.util.List;

public interface AlunoAvaliacaoService {

    List<AlunoAvaliacao> buscarTodosAlunosAvaliacoes();

    AlunoAvaliacao buscarAlunoAvaliacaoPorIdAlunoAvaliacao(Long idAlunoAvaliacao);

    AlunoAvaliacao criarAlunoAvaliacao(AlunoAvaliacao alunoAvaliacao);

    AlunoAvaliacao atualizarAlunoAvaliacao(AlunoAvaliacao alunoAvaliacaoDoBanco);

    AlunoAvaliacao deletarAlunoAvaliacao(AlunoAvaliacao alunoAvaliacao);

}
