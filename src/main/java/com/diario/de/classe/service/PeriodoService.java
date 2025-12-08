package com.diario.de.classe.service;

import com.diario.de.classe.model.Periodo;
import java.util.List;

public interface PeriodoService {

    List<Periodo> buscarTodosPeriodos();

    Periodo buscarPeriodoPoridPeriodo(Long idPeriodo);

    Periodo criarPeriodo(Periodo periodo);

    Periodo atualizarPeriodo(Long idPeriodo, Periodo periodo, Periodo periodoDoBanco);

    Periodo deletarPeriodo(Periodo periodo);

} 