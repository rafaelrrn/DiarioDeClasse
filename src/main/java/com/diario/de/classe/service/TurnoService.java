package com.diario.de.classe.service;

import com.diario.de.classe.model.Turno;
import java.util.List;

public interface TurnoService {

    List<Turno> buscarTodosTurnos();

    Turno buscarTurnoPoridTurno(Long idTurno);

    Turno criarTurno(Turno turno);

    Turno atualizarTurno(Long idTurno, Turno turno, Turno turnoDoBanco);

    Turno deletarTurno(Turno turno);

} 