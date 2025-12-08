package com.diario.de.classe.service.old;

import com.diario.de.classe.model.old.ClasseEscolar;

import java.util.List;

public interface ClasseEscolarService {

    List<ClasseEscolar> buscarTodasClasses();

    ClasseEscolar buscarClassePorCodCls(Long codCls);

    ClasseEscolar criarClasse(String classe);

    ClasseEscolar atualizarClasse(Long codCls, String classeEscolarBody, ClasseEscolar classeEscolarDoBanco);

    ClasseEscolar deletarClasse(ClasseEscolar classeEscolar);

}
