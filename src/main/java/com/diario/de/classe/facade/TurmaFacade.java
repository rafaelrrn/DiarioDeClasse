package com.diario.de.classe.facade;

import com.diario.de.classe.dto.TurmaDTO;
import org.springframework.http.ResponseEntity;

public interface TurmaFacade {
    ResponseEntity<Object> buscarTodasTurmas();

    ResponseEntity<Object> buscarTurmaPoridTurma(Long idTurma);

    ResponseEntity<Object> criarTurma(TurmaDTO turmaDTO);

    ResponseEntity<Object> atualizarTurma(Long idTurma, TurmaDTO turmaDTO);

    ResponseEntity<Object> deletarTurma(Long idTurma);

}
