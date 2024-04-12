package com.diario.de.classe.facade;

import org.springframework.http.ResponseEntity;

public interface ClasseEscolarFacade {
    ResponseEntity<Object> buscarTodasClasses();

    ResponseEntity<Object> buscarClassePorCodCls(Long codCls);

    ResponseEntity<Object> criarClasse(String classe);

    ResponseEntity<Object> atualizarClasse(Long codCls, String classeEscolarBody);

    ResponseEntity<Object> deletarClasse(Long codCls);

}
