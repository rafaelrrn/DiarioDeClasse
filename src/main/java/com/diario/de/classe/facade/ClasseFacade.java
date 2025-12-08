package com.diario.de.classe.facade;

import com.diario.de.classe.dto.ClasseDTO;
import org.springframework.http.ResponseEntity;

public interface ClasseFacade {
    ResponseEntity<Object> buscarTodasClasses();

    ResponseEntity<Object> buscarClassePoridClasse(Long idClasse);

    ResponseEntity<Object> criarClasse(ClasseDTO classeDTO);

    ResponseEntity<Object> atualizarClasse(Long idClasse, ClasseDTO classeDTO);

    ResponseEntity<Object> deletarClasse(Long idClasse);

}
