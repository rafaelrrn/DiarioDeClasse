package com.diario.de.classe.service;

import com.diario.de.classe.model.Ensino;
import java.util.List;

public interface EnsinoService {

    List<Ensino> buscarTodosEnsinos();

    Ensino buscarEnsinoPoridEnsino(Long idEnsino);

    Ensino criarEnsino(Ensino ensino);

    Ensino atualizarEnsino(Long idEnsino, Ensino ensino, Ensino ensinoDoBanco);

    Ensino deletarEnsino(Ensino ensino);

} 