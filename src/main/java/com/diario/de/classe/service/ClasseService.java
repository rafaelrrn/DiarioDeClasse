package com.diario.de.classe.service;

import com.diario.de.classe.model.Classe;

import java.util.List;

public interface ClasseService {

    List<Classe> buscarTodasClasses();

    Classe buscarClassePoridClasse(Long idClasse);

    Classe criarClasse(Classe classe);

    Classe atualizarClasse(Classe classeDoBanco);

    Classe deletarClasse(Classe classe);

}
