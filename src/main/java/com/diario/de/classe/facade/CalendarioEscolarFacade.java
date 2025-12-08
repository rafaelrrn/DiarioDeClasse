package com.diario.de.classe.facade;

import com.diario.de.classe.dto.CalendarioEscolarDTO;
import org.springframework.http.ResponseEntity;

public interface CalendarioEscolarFacade {
    ResponseEntity<Object> buscarTodosCalendariosEscolares();

    ResponseEntity<Object> buscarCalendarioEscolarPoridCalendarioEscolar(Long idCalendarioEscolar);

    ResponseEntity<Object> criarCalendarioEscolar(CalendarioEscolarDTO calendarioEscolarDTO);

    ResponseEntity<Object> atualizarCalendarioEscolar(Long idCalendarioEscolar, CalendarioEscolarDTO calendarioEscolarDTO);

    ResponseEntity<Object> deletarCalendarioEscolar(Long idCalendarioEscolar);

}
