package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Periodo;
import com.diario.de.classe.populator.PeriodoPopulator;
import com.diario.de.classe.repository.jpa.PeriodoRepositoryJpa;
import com.diario.de.classe.service.PeriodoService;
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
public class PeriodoServiceImpl implements PeriodoService {
    final private static Logger LOG = LogManager.getLogger(PeriodoServiceImpl.class);

    @Autowired
    PeriodoRepositoryJpa periodoRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    PeriodoPopulator periodoPopulator;

    @Override
    public List<Periodo> buscarTodosPeriodos() {
        return periodoRepositoryJpa.findAll();
    }

    @Override
    public Periodo buscarPeriodoPoridPeriodo(Long idPeriodo) {
        Optional<Periodo> periodo = periodoRepositoryJpa.findById(idPeriodo);
        return periodo.orElse(null);
    }

    @Override
    public Periodo criarPeriodo(Periodo periodo) {
        if (!ObjectUtils.isEmpty(periodo)) {
            return periodoRepositoryJpa.save(periodo);
        }
        return periodo;
    }

    @Override
    public Periodo atualizarPeriodo(Long idPeriodo, Periodo periodo, Periodo periodoDoBanco) {
        BeanUtils.copyProperties(periodo, periodoDoBanco);
        return periodoRepositoryJpa.save(periodoDoBanco);
    }

    @Override
    public Periodo deletarPeriodo(Periodo periodo) {
        periodoRepositoryJpa.delete(periodo);
        return periodo;
    }
} 