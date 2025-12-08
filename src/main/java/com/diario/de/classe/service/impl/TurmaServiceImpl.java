package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Turma;
import com.diario.de.classe.populator.TurmaPopulator;
import com.diario.de.classe.repository.jpa.TurmaRepositoryJpa;
import com.diario.de.classe.service.TurmaService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class TurmaServiceImpl implements TurmaService {
    final private static Logger LOG = LogManager.getLogger(TurmaServiceImpl.class);

    @Autowired
    TurmaRepositoryJpa turmaRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    TurmaPopulator turmaPopulator;

    @Override
    public List<Turma> buscarTodasTurmas() {
        return turmaRepositoryJpa.findAll();
    }

    @Override
    public Turma buscarTurmaPoridTurma(Long idTurma) {
        Optional<Turma> turma = turmaRepositoryJpa.findById(idTurma);
        return turma.orElse(null);
    }

    @Override
    public Turma criarTurma(Turma turma) {
        if (!ObjectUtils.isEmpty(turma)) {
            return turmaRepositoryJpa.save(turma);
        }
        return turma;
    }

    @Override
    public Turma atualizarTurma(Long idTurma, Turma turma, Turma turmaDoBanco) {
        BeanUtils.copyProperties(turma, turmaDoBanco);
        return turmaRepositoryJpa.save(turmaDoBanco);
    }

    @Override
    public Turma deletarTurma(Turma turma) {
        turmaRepositoryJpa.delete(turma);
        return turma;
    }
} 