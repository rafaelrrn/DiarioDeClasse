package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.CalendarioEscolar;
import com.diario.de.classe.repository.jpa.CalendarioEscolarRepositoryJpa;
import com.diario.de.classe.service.CalendarioEscolarService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CalendarioEscolarServiceImpl implements CalendarioEscolarService {
    final private static Logger LOG = LogManager.getLogger(CalendarioEscolarServiceImpl.class);

    @Autowired
    private CalendarioEscolarRepositoryJpa calendarioEscolarRepositoryJpa;

    @Override
    public List<CalendarioEscolar> buscarTodosCalendariosEscolares() {
        return calendarioEscolarRepositoryJpa.findAll();
    }

    @Override
    public CalendarioEscolar buscarCalendarioEscolarPoridCalendarioEscolar(Long idCalendarioEscolar) {
        Optional<CalendarioEscolar> calendarioEscolar = calendarioEscolarRepositoryJpa.findById(idCalendarioEscolar);
        return calendarioEscolar.orElse(null);
    }

    @Override
    public CalendarioEscolar criarCalendarioEscolar(CalendarioEscolar calendarioEscolar) {
        if (!ObjectUtils.isEmpty(calendarioEscolar)) {
            return calendarioEscolarRepositoryJpa.save(calendarioEscolar);
        }
        return calendarioEscolar;
    }

    @Override
    public CalendarioEscolar atualizarCalendarioEscolar(CalendarioEscolar calendarioEscolarDoBanco) {
        if (!ObjectUtils.isEmpty(calendarioEscolarDoBanco)) {
            return calendarioEscolarRepositoryJpa.save(calendarioEscolarDoBanco);
        }
        return calendarioEscolarDoBanco;
    }

    @Override
    public CalendarioEscolar deletarCalendarioEscolar(CalendarioEscolar calendarioEscolar) {
        calendarioEscolarRepositoryJpa.delete(calendarioEscolar);
        return calendarioEscolar;
    }
} 