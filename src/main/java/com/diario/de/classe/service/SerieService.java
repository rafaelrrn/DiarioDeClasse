package com.diario.de.classe.service;

import com.diario.de.classe.model.Serie;
import java.util.List;

public interface SerieService {

    List<Serie> buscarTodasSeries();

    Serie buscarSeriePoridSerie(Long idSerie);

    Serie criarSerie(Serie serie);

    Serie atualizarSerie(Long idSerie, Serie serie, Serie serieDoBanco);

    Serie deletarSerie(Serie serie);

} 