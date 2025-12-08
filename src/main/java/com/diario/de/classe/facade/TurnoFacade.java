package com.diario.de.classe.facade;

import com.diario.de.classe.dto.TurnoDTO;
import org.springframework.http.ResponseEntity;

public interface TurnoFacade {
    ResponseEntity<Object> buscarTodosTurnos();

    ResponseEntity<Object> buscarTurnoPoridTurno(Long idTurno);

    ResponseEntity<Object> criarTurno(TurnoDTO turnoDTO);

    ResponseEntity<Object> atualizarTurno(Long idTurno, TurnoDTO turnoDTO);

    ResponseEntity<Object> deletarTurno(Long idTurno);

}
