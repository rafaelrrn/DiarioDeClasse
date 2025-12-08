package com.diario.de.classe.facade;

import com.diario.de.classe.dto.PeriodoDTO;
import org.springframework.http.ResponseEntity;

public interface PeriodoFacade {
    ResponseEntity<Object> buscarTodosPeriodos();

    ResponseEntity<Object> buscarPeriodoPoridPeriodo(Long idPeriodo);

    ResponseEntity<Object> criarPeriodo(PeriodoDTO periodoDTO);

    ResponseEntity<Object> atualizarPeriodo(Long idPeriodo, PeriodoDTO periodoDTO);

    ResponseEntity<Object> deletarPeriodo(Long idPeriodo);

}
