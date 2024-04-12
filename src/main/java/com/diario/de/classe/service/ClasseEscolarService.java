package com.diario.de.classe.service;

import com.diario.de.classe.model.ClasseEscolar;

import java.util.List;

public interface ClasseEscolarService {

    List<ClasseEscolar> buscarTodasClasses();

    ClasseEscolar buscarClassePorCodCls(Long codCls);

    ClasseEscolar criarClasse(String classe);

    ClasseEscolar atualizarClasse(Long codCls, String classeEscolarBody, ClasseEscolar classeEscolarDoBanco);

    ClasseEscolar deletarClasse(ClasseEscolar classeEscolar);

}
