package com.diario.de.classe.facade;

import com.diario.de.classe.dto.AnoCalendarioDTO;
import org.springframework.http.ResponseEntity;

public interface AnoCalendarioFacade {
    ResponseEntity<Object> buscarTodosAnosCalendarios();

    ResponseEntity<Object> buscarAnoCalendarioPoridAnoCalendario(Long idAnoCalendario);

    ResponseEntity<Object> criarAnoCalendario(AnoCalendarioDTO anoCalendarioDTO);

    ResponseEntity<Object> atualizarAnoCalendario(Long idAnoCalendario, AnoCalendarioDTO anoCalendarioDTO);

    ResponseEntity<Object> deletarAnoCalendario(Long idAnoCalendario);

}
