package com.diario.de.classe.service;

import com.diario.de.classe.model.AlunoAvaliacao;
import com.diario.de.classe.model.AnoCalendario;

import java.util.List;

public interface AnoCalendarioService {

    List<AnoCalendario> buscarTodosAnosCalendarios();

    AnoCalendario buscarAnoCalendarioPoridAnoCalendario(Long idAnoCalendario);

    AnoCalendario criarAnoCalendario(AnoCalendario anoCalendario);

    AnoCalendario atualizarAnoCalendario(Long idAnoCalendario, AnoCalendario anoCalendario, AnoCalendario anoCalendarioDoBanco);

    AnoCalendario deletarAnoCalendario(AnoCalendario anoCalendario);

}
