package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Ensino;
import com.diario.de.classe.populator.EnsinoPopulator;
import com.diario.de.classe.repository.jpa.EnsinoRepositoryJpa;
import com.diario.de.classe.service.EnsinoService;
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
public class EnsinoServiceImpl implements EnsinoService {
    final private static Logger LOG = LogManager.getLogger(EnsinoServiceImpl.class);

    @Autowired
    EnsinoRepositoryJpa ensinoRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    EnsinoPopulator ensinoPopulator;

    @Override
    public List<Ensino> buscarTodosEnsinos() {
        return ensinoRepositoryJpa.findAll();
    }

    @Override
    public Ensino buscarEnsinoPoridEnsino(Long idEnsino) {
        Optional<Ensino> ensino = ensinoRepositoryJpa.findById(idEnsino);
        return ensino.orElse(null);
    }

    @Override
    public Ensino criarEnsino(Ensino ensino) {
        if (!ObjectUtils.isEmpty(ensino)) {
            return ensinoRepositoryJpa.save(ensino);
        }
        return ensino;
    }

    @Override
    public Ensino atualizarEnsino(Long idEnsino, Ensino ensino, Ensino ensinoDoBanco) {
        BeanUtils.copyProperties(ensino, ensinoDoBanco);
        return ensinoRepositoryJpa.save(ensinoDoBanco);
    }

    @Override
    public Ensino deletarEnsino(Ensino ensino) {
        ensinoRepositoryJpa.delete(ensino);
        return ensino;
    }
} 