package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.AlunoTurma;
import com.diario.de.classe.repository.jpa.AlunoTurmaRepositoryJpa;
import com.diario.de.classe.service.AlunoTurmaService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import com.diario.de.classe.populator.AlunoTurmaPopulator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoTurmaServiceImpl implements AlunoTurmaService {
    final private static Logger LOG = LogManager.getLogger(AlunoTurmaServiceImpl.class);

    @Autowired
    private AlunoTurmaRepositoryJpa alunoTurmaRepositoryJpa;
    @Autowired
    private ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    private AlunoTurmaPopulator alunoTurmaPopulator;

    @Override
    public List<AlunoTurma> buscarTodosAlunosTurmas() {
        return alunoTurmaRepositoryJpa.findAll();
    }

    @Override
    public AlunoTurma buscarAlunoTurmaPorIdAlunoTurma(Long idAlunoTurma) {
        Optional<AlunoTurma> alunoTurma = alunoTurmaRepositoryJpa.findById(idAlunoTurma);
        return alunoTurma.orElse(null);
    }

    @Override
    public AlunoTurma criarAlunoTurma(AlunoTurma alunoTurma) {
        if (!ObjectUtils.isEmpty(alunoTurma)) {
            return alunoTurmaRepositoryJpa.save(alunoTurma);
        }
        return alunoTurma;
    }

    @Override
    public AlunoTurma atualizarAlunoTurma(AlunoTurma alunoTurmaDoBanco) {
        if (!ObjectUtils.isEmpty(alunoTurmaDoBanco)) {
            return alunoTurmaRepositoryJpa.save(alunoTurmaDoBanco);
        }
        return alunoTurmaDoBanco;
    }

    @Override
    public AlunoTurma deletarAlunoTurma(AlunoTurma alunoTurma) {
        alunoTurmaRepositoryJpa.delete(alunoTurma);
        return alunoTurma;
    }
} 