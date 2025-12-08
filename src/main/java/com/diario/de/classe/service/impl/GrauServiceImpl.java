package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Grau;
import com.diario.de.classe.populator.GrauPopulator;
import com.diario.de.classe.repository.jpa.GrauRepositoryJpa;
import com.diario.de.classe.service.GrauService;
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
public class GrauServiceImpl implements GrauService {
    final private static Logger LOG = LogManager.getLogger(GrauServiceImpl.class);

    @Autowired
    GrauRepositoryJpa grauRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    GrauPopulator grauPopulator;

    @Override
    public List<Grau> buscarTodosGraus() {
        return grauRepositoryJpa.findAll();
    }

    @Override
    public Grau buscarGrauPoridGrau(Long idGrau) {
        Optional<Grau> grau = grauRepositoryJpa.findById(idGrau);
        return grau.orElse(null);
    }

    @Override
    public Grau criarGrau(Grau grau) {
        if (!ObjectUtils.isEmpty(grau)) {
            return grauRepositoryJpa.save(grau);
        }
        return grau;
    }

    @Override
    public Grau atualizarGrau(Long idGrau, Grau grau, Grau grauDoBanco) {
        BeanUtils.copyProperties(grau, grauDoBanco);
        return grauRepositoryJpa.save(grauDoBanco);
    }

    @Override
    public Grau deletarGrau(Grau grau) {
        grauRepositoryJpa.delete(grau);
        return grau;
    }
} 