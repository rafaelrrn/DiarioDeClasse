package com.diario.de.classe.service;

import com.diario.de.classe.model.Avaliacao;

import java.util.List;

public interface AvaliacaoService {

    List<Avaliacao> buscarTodoasAvaliacoes();

    Avaliacao buscarAvaliacaoPoridAvaliacao(Long idAvaliacao);

    Avaliacao criarAvaliacao(Avaliacao avaliacao);

    Avaliacao atualizarAvaliacao(Avaliacao avaliacaoDoBanco);

    Avaliacao deletarAvaliacao(Avaliacao avaliacao);

}
