package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.AnoCalendario;
import com.diario.de.classe.repository.jpa.AnoCalendarioRepositoryJpa;
import com.diario.de.classe.service.AnoCalendarioService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import com.diario.de.classe.populator.AnoCalendarioPopulator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AnoCalendarioServiceImpl implements AnoCalendarioService {
    final private static Logger LOG = LogManager.getLogger(AnoCalendarioServiceImpl.class);

    @Autowired
    private AnoCalendarioRepositoryJpa anoCalendarioRepositoryJpa;
    @Autowired
    private ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    private AnoCalendarioPopulator anoCalendarioPopulator;

    @Override
    public List<AnoCalendario> buscarTodosAnosCalendarios() {
        return anoCalendarioRepositoryJpa.findAll();
    }

    @Override
    public AnoCalendario buscarAnoCalendarioPoridAnoCalendario(Long idAnoCalendario) {
        Optional<AnoCalendario> anoCalendario = anoCalendarioRepositoryJpa.findById(idAnoCalendario);
        return anoCalendario.orElse(null);
    }

    @Override
    public AnoCalendario criarAnoCalendario(AnoCalendario anoCalendario) {
        if (!ObjectUtils.isEmpty(anoCalendario)) {
            return anoCalendarioRepositoryJpa.save(anoCalendario);
        }
        return anoCalendario;
    }

    @Override
    public AnoCalendario atualizarAnoCalendario(Long idAnoCalendario, AnoCalendario anoCalendario, AnoCalendario anoCalendarioDoBanco) {
        BeanUtils.copyProperties(anoCalendario, anoCalendarioDoBanco);
        return anoCalendarioRepositoryJpa.save(anoCalendarioDoBanco);
    }

    @Override
    public AnoCalendario deletarAnoCalendario(AnoCalendario anoCalendario) {
        anoCalendarioRepositoryJpa.delete(anoCalendario);
        return anoCalendario;
    }
} 