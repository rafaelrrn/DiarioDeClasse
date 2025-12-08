package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Disciplina;
import com.diario.de.classe.populator.DisciplinaPopulator;
import com.diario.de.classe.repository.jpa.DisciplinaRepositoryJpa;
import com.diario.de.classe.service.DisciplinaService;
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
public class DisciplinaServiceImpl implements DisciplinaService {
    final private static Logger LOG = LogManager.getLogger(DisciplinaServiceImpl.class);

    @Autowired
    DisciplinaRepositoryJpa disciplinaRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    DisciplinaPopulator disciplinaPopulator;

    @Override
    public List<Disciplina> buscarTodasDisciplinas() {
        return disciplinaRepositoryJpa.findAll();
    }

    @Override
    public Disciplina buscarDisciplinaPoridDisciplina(Long idDisciplina) {
        Optional<Disciplina> disciplina = disciplinaRepositoryJpa.findById(idDisciplina);
        return disciplina.orElse(null);
    }

    @Override
    public Disciplina criarDisciplina(Disciplina disciplina) {
        if (!ObjectUtils.isEmpty(disciplina)) {
            return disciplinaRepositoryJpa.save(disciplina);
        }
        return disciplina;
    }

    @Override
    public Disciplina atualizarDisciplina(Long idDisciplina, Disciplina disciplina, Disciplina disciplinaDoBanco) {
        BeanUtils.copyProperties(disciplina, disciplinaDoBanco);
        return disciplinaRepositoryJpa.save(disciplinaDoBanco);
    }

    @Override
    public Disciplina deletarDisciplina(Disciplina disciplina) {
        disciplinaRepositoryJpa.delete(disciplina);
        return disciplina;
    }
} 