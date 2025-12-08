package com.diario.de.classe.facade;

import com.diario.de.classe.dto.SerieDTO;
import org.springframework.http.ResponseEntity;

public interface SerieFacade {
    ResponseEntity<Object> buscarTodasSeries();

    ResponseEntity<Object> buscarSeriePoridSerie(Long idSerie);

    ResponseEntity<Object> criarSerie(SerieDTO serieDTO);

    ResponseEntity<Object> atualizarSerie(Long idSerie, SerieDTO serieDTO);

    ResponseEntity<Object> deletarSerie(Long idSerie);

}
