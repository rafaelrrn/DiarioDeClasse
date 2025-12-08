package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.AlunoAvaliacao;
import com.diario.de.classe.repository.jpa.AlunoAvaliacaoRepositoryJpa;
import com.diario.de.classe.service.AlunoAvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoAvaliacaoServiceImpl implements AlunoAvaliacaoService {
    @Autowired
    private AlunoAvaliacaoRepositoryJpa alunoAvaliacaoRepositoryJpa;

    @Override
    public List<AlunoAvaliacao> buscarTodosAlunosAvaliacoes() {
        return alunoAvaliacaoRepositoryJpa.findAll();
    }

    @Override
    public AlunoAvaliacao buscarAlunoAvaliacaoPorIdAlunoAvaliacao(Long idAlunoAvaliacao) {
        Optional<AlunoAvaliacao> alunoAvaliacao = alunoAvaliacaoRepositoryJpa.findById(idAlunoAvaliacao);
        return alunoAvaliacao.orElse(null);
    }

    @Override
    public AlunoAvaliacao criarAlunoAvaliacao(AlunoAvaliacao alunoAvaliacao) {
        if (!ObjectUtils.isEmpty(alunoAvaliacao)) {
            return alunoAvaliacaoRepositoryJpa.save(alunoAvaliacao);
        }
        return alunoAvaliacao;
    }

    @Override
    public AlunoAvaliacao atualizarAlunoAvaliacao(AlunoAvaliacao alunoAvaliacaoDoBanco) {
        if (!ObjectUtils.isEmpty(alunoAvaliacaoDoBanco)) {
            return alunoAvaliacaoRepositoryJpa.save(alunoAvaliacaoDoBanco);
        }
        return alunoAvaliacaoDoBanco;
    }

    @Override
    public AlunoAvaliacao deletarAlunoAvaliacao(AlunoAvaliacao alunoAvaliacao) {
        alunoAvaliacaoRepositoryJpa.delete(alunoAvaliacao);
        return alunoAvaliacao;
    }
} 