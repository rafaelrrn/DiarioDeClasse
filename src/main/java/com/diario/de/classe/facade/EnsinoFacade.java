package com.diario.de.classe.facade;

import com.diario.de.classe.dto.EnsinoDTO;
import org.springframework.http.ResponseEntity;

public interface EnsinoFacade {
    ResponseEntity<Object> buscarTodosEnsinos();

    ResponseEntity<Object> buscarEnsinoPoridEnsino(Long idEnsino);

    ResponseEntity<Object> criarEnsino(EnsinoDTO ensinoDTO);

    ResponseEntity<Object> atualizarEnsino(Long idEnsino, EnsinoDTO ensinoDTO);

    ResponseEntity<Object> deletarEnsino(Long idEnsino);

}
