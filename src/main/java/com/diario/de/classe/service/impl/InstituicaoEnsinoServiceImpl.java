package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.InstituicaoEnsino;
import com.diario.de.classe.populator.InstituicaoEnsinoPopulator;
import com.diario.de.classe.repository.jpa.InstituicaoEnsinoRepositoryJpa;
import com.diario.de.classe.service.InstituicaoEnsinoService;
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
public class InstituicaoEnsinoServiceImpl implements InstituicaoEnsinoService {
    final private static Logger LOG = LogManager.getLogger(InstituicaoEnsinoServiceImpl.class);

    @Autowired
    InstituicaoEnsinoRepositoryJpa instituicaoEnsinoRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    InstituicaoEnsinoPopulator instituicaoEnsinoPopulator;

    @Override
    public List<InstituicaoEnsino> buscarTodasInstituicoesEnsino() {
        return instituicaoEnsinoRepositoryJpa.findAll();
    }

    @Override
    public InstituicaoEnsino buscarInstituicaoEnsinoPoridInstituicaoEnsino(Long idInstituicaoEnsino) {
        Optional<InstituicaoEnsino> instituicaoEnsino = instituicaoEnsinoRepositoryJpa.findById(idInstituicaoEnsino);
        return instituicaoEnsino.orElse(null);
    }

    @Override
    public InstituicaoEnsino criarInstituicaoEnsino(InstituicaoEnsino instituicaoEnsino) {
        if (!ObjectUtils.isEmpty(instituicaoEnsino)) {
            return instituicaoEnsinoRepositoryJpa.save(instituicaoEnsino);
        }
        return instituicaoEnsino;
    }

    @Override
    public InstituicaoEnsino atualizarInstituicaoEnsino(Long idInstituicaoEnsino, InstituicaoEnsino instituicaoEnsino, InstituicaoEnsino instituicaoEnsinoDoBanco) {
        BeanUtils.copyProperties(instituicaoEnsino, instituicaoEnsinoDoBanco);
        return instituicaoEnsinoRepositoryJpa.save(instituicaoEnsinoDoBanco);
    }

    @Override
    public InstituicaoEnsino deletarInstituicaoEnsino(InstituicaoEnsino instituicaoEnsino) {
        instituicaoEnsinoRepositoryJpa.delete(instituicaoEnsino);
        return instituicaoEnsino;
    }
} 