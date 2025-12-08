package com.diario.de.classe.service;

import com.diario.de.classe.model.Grau;
import java.util.List;

public interface GrauService {

    List<Grau> buscarTodosGraus();

    Grau buscarGrauPoridGrau(Long idGrau);

    Grau criarGrau(Grau grau);

    Grau atualizarGrau(Long idGrau, Grau grau, Grau grauDoBanco);

    Grau deletarGrau(Grau grau);

} 