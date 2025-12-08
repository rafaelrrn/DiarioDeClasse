package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Serie;
import com.diario.de.classe.populator.SeriePopulator;
import com.diario.de.classe.repository.jpa.SerieRepositoryJpa;
import com.diario.de.classe.service.SerieService;
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
public class SerieServiceImpl implements SerieService {
    final private static Logger LOG = LogManager.getLogger(SerieServiceImpl.class);

    @Autowired
    SerieRepositoryJpa serieRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    SeriePopulator seriePopulator;

    @Override
    public List<Serie> buscarTodasSeries() {
        return serieRepositoryJpa.findAll();
    }

    @Override
    public Serie buscarSeriePoridSerie(Long idSerie) {
        Optional<Serie> serie = serieRepositoryJpa.findById(idSerie);
        return serie.orElse(null);
    }

    @Override
    public Serie criarSerie(Serie serie) {
        if (!ObjectUtils.isEmpty(serie)) {
            return serieRepositoryJpa.save(serie);
        }
        return serie;
    }

    @Override
    public Serie atualizarSerie(Long idSerie, Serie serie, Serie serieDoBanco) {
        BeanUtils.copyProperties(serie, serieDoBanco);
        return serieRepositoryJpa.save(serieDoBanco);
    }

    @Override
    public Serie deletarSerie(Serie serie) {
        serieRepositoryJpa.delete(serie);
        return serie;
    }
} 