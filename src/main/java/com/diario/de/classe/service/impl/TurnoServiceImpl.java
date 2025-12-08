package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Turno;
import com.diario.de.classe.populator.TurnoPopulator;
import com.diario.de.classe.repository.jpa.TurnoRepositoryJpa;
import com.diario.de.classe.service.TurnoService;
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
public class TurnoServiceImpl implements TurnoService {
    final private static Logger LOG = LogManager.getLogger(TurnoServiceImpl.class);

    @Autowired
    TurnoRepositoryJpa turnoRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    TurnoPopulator turnoPopulator;

    @Override
    public List<Turno> buscarTodosTurnos() {
        return turnoRepositoryJpa.findAll();
    }

    @Override
    public Turno buscarTurnoPoridTurno(Long idTurno) {
        Optional<Turno> turno = turnoRepositoryJpa.findById(idTurno);
        return turno.orElse(null);
    }

    @Override
    public Turno criarTurno(Turno turno) {
        if (!ObjectUtils.isEmpty(turno)) {
            return turnoRepositoryJpa.save(turno);
        }
        return turno;
    }

    @Override
    public Turno atualizarTurno(Long idTurno, Turno turno, Turno turnoDoBanco) {
        BeanUtils.copyProperties(turno, turnoDoBanco);
        return turnoRepositoryJpa.save(turnoDoBanco);
    }

    @Override
    public Turno deletarTurno(Turno turno) {
        turnoRepositoryJpa.delete(turno);
        return turno;
    }
} 