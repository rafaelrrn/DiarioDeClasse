package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Avaliacao;
import com.diario.de.classe.repository.jpa.AvaliacaoRepositoryJpa;
import com.diario.de.classe.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoServiceImpl implements AvaliacaoService {
    @Autowired
    private AvaliacaoRepositoryJpa avaliacaoRepositoryJpa;

    @Override
    public List<Avaliacao> buscarTodoasAvaliacoes() {
        return avaliacaoRepositoryJpa.findAll();
    }

    @Override
    public Avaliacao buscarAvaliacaoPoridAvaliacao(Long idAvaliacao) {
        Optional<Avaliacao> avaliacao = avaliacaoRepositoryJpa.findById(idAvaliacao);
        return avaliacao.orElse(null);
    }

    @Override
    public Avaliacao criarAvaliacao(Avaliacao avaliacao) {
        if (!ObjectUtils.isEmpty(avaliacao)) {
            return avaliacaoRepositoryJpa.save(avaliacao);
        }
        return avaliacao;
    }

    @Override
    public Avaliacao atualizarAvaliacao(Avaliacao avaliacaoDoBanco) {
        if (!ObjectUtils.isEmpty(avaliacaoDoBanco)) {
            return avaliacaoRepositoryJpa.save(avaliacaoDoBanco);
        }
        return avaliacaoDoBanco;
    }

    @Override
    public Avaliacao deletarAvaliacao(Avaliacao avaliacao) {
        avaliacaoRepositoryJpa.delete(avaliacao);
        return avaliacao;
    }
} 