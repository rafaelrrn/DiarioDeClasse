package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Mes;
import com.diario.de.classe.populator.MesPopulator;
import com.diario.de.classe.repository.jpa.MesRepositoryJpa;
import com.diario.de.classe.service.MesService;
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
public class MesServiceImpl implements MesService {
    final private static Logger LOG = LogManager.getLogger(MesServiceImpl.class);

    @Autowired
    MesRepositoryJpa mesRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    MesPopulator mesPopulator;

    @Override
    public List<Mes> buscarTodosMeses() {
        return mesRepositoryJpa.findAll();
    }

    @Override
    public Mes buscarMesPoridMes(Long idMes) {
        Optional<Mes> mes = mesRepositoryJpa.findById(idMes);
        return mes.orElse(null);
    }

    @Override
    public Mes criarMes(Mes mes) {
        if (!ObjectUtils.isEmpty(mes)) {
            return mesRepositoryJpa.save(mes);
        }
        return mes;
    }

    @Override
    public Mes atualizarMes(Long idMes, Mes mes, Mes mesDoBanco) {
        BeanUtils.copyProperties(mes, mesDoBanco);
        return mesRepositoryJpa.save(mesDoBanco);
    }

    @Override
    public Mes deletarMes(Mes mes) {
        mesRepositoryJpa.delete(mes);
        return mes;
    }
} 