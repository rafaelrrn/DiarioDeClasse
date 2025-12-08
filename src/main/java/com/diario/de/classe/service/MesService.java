package com.diario.de.classe.service;

import com.diario.de.classe.model.Mes;
import java.util.List;

public interface MesService {

    List<Mes> buscarTodosMeses();

    Mes buscarMesPoridMes(Long idMes);

    Mes criarMes(Mes mes);

    Mes atualizarMes(Long idMes, Mes mes, Mes mesDoBanco);

    Mes deletarMes(Mes mes);

} 