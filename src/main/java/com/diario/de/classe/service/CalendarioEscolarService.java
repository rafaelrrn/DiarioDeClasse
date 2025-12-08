package com.diario.de.classe.service;

import com.diario.de.classe.model.CalendarioEscolar;

import java.util.List;

public interface CalendarioEscolarService {

    List<CalendarioEscolar> buscarTodosCalendariosEscolares();

    CalendarioEscolar buscarCalendarioEscolarPoridCalendarioEscolar(Long idCalendarioEscolar);

    CalendarioEscolar criarCalendarioEscolar(CalendarioEscolar calendarioEscolar);

    CalendarioEscolar atualizarCalendarioEscolar(CalendarioEscolar calendarioEscolarDoBanco);

    CalendarioEscolar deletarCalendarioEscolar(CalendarioEscolar calendarioEscolar);

}
